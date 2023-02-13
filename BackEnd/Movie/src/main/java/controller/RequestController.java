package controller;

import jwt.JWT;
import movie.components.Actor;
import movie.components.Comment;
import movie.components.Director;
import movie.components.Movie;

import responses.ActorResForm;
import responses.DirectorResFrom;
import responses.MoviesResForm;
import responses.UserRegisterForm;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.plugin.json.JavalinJackson;
import movie.production.CombinedMovieComponents;
import movie.production.Helper;
import movie.production.UserConnection;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RequestController implements Helper {
    private static RequestController controller;
    private static CombinedMovieComponents components;
    private static UserConnection userConnection;
    private static JWT jwt = new JWT();
    private final String AccessControl = "Access-Control-Allow-Origin";
    private final String allowedPort = "*";

    public static RequestController getInstance(CombinedMovieComponents comp) {
        if (controller == null) {
            controller = new RequestController();
            components = comp;
            userConnection = new UserConnection();
        }
        return controller;
    }

    public Handler getMovies = context -> {
            List<MoviesResForm> movies;
            context.header(AccessControl, allowedPort);
            String name = context.req.getParameter("name");
            if (name == null) {
                movies = components.getAllMovies();

            } else {
                movies = components.getMoviesByName(name);
            }
        context.json(movies);
    };


    public Handler test = context -> {
        context.header(AccessControl, allowedPort);
        System.out.println(context.body());
    };

    public Handler getOneMovie = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            if (id != null) {
                Movie movie = components.getOneMovie(Integer.parseInt(id));
                context.json(movie);
            }
        }
    };

    public Handler getOneActor = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            if (id != null) {
                ActorResForm from = components.getOneActor(Integer.parseInt(id));
                context.json(from);
            }
        }
    };

    public Handler getOneDirector = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            if (id != null) {
                DirectorResFrom form = components.getOneDirector(Integer.parseInt(id));
                context.json(form);
            }
        }
    };
    public Handler getActors = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String name = context.req.getParameter("name");
            String country = context.req.getParameter("country");
            String gender = context.req.getParameter("gender");
            String year = context.req.getParameter("year");
            String yearCond = context.req.getParameter("cond");
            Map<String, String> params = getParams(name, country, gender, year, yearCond);
            List<Actor> actors;
            if (params.isEmpty())
                actors = components.getActors();
            else
                actors = components.getActors(params);

            context.json(actors);
        }
    };

    public Handler getDirectors = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String name = context.req.getParameter("name");
            String country = context.req.getParameter("country");
            String gender = context.req.getParameter("gender");
            String year = context.req.getParameter("year");
            String yearCond = context.req.getParameter("cond");
            Map<String, String> params = getParams(name, country, gender, year, yearCond);
            List<Director> directors;
            if (params.isEmpty())
                directors = components.getDirectors();
            else
                directors = components.getDirectors(params);
            context.json(directors);
        }
    };

    //ctx.header(Header.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true")

    public Handler registerUser = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            JavalinJackson jackson = new JavalinJackson();
            UserRegisterForm form;
            try {
                form = jackson.fromJsonString(context.body(), UserRegisterForm.class);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                return;
            }
            String username = form.getUsername();
            Map<String, String> response = new HashMap<>();
            if (userConnection.userExists(username)) {
                context.res.setStatus(300);
                response.put("status", "300");
                context.json(response);
            } else {
                context.res.setStatus(200);
                response.put("status", "200");
                context.json(response);
                userConnection.addUser(form);
            }
        }
    };

    public Handler getUser = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String username = context.req.getParameter("name");
            String password = context.req.getParameter("pass");
            Map<String, String> response = new HashMap<>();
            int user_id = userConnection.getUserID(username);
            JWT jwt = new JWT();
            String token = jwt.generateJWT(String.valueOf(user_id), username, 900000L);
            if (userConnection.userExists(username)) {
                if (userConnection.matchPassword(username, password)) {
                    context.res.setStatus(200);
                    response.put("name", username);
                    response.put("jwt", token);
                    response.put("status", "200");
                    context.json(response);
                } else {
                    context.res.setStatus(311);
                    response.put("status", "311");
                    context.json(response);
                }
            } else {
                context.res.setStatus(310);
                response.put("status", "310");
                context.json(response);
            }
        }
    };

    public Handler getFilteredMovies = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String genre = context.req.getParameter("genre");
            String rate = context.req.getParameter("rate");
            String year = context.req.getParameter("year");
            String cond = context.req.getParameter("cond");
            String celebrity = context.req.getParameter("cel");
            String name = context.req.getParameter("name");
            Map<String, String> params = movieParamList(genre, rate, year, cond, celebrity, name);
            List<MoviesResForm> movies = components.getMoviesByParams(params);
            context.json(movies);
        }
    };

    public Handler checkIsMovieRated = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            String username = context.req.getParameter("name");
            int rating = userConnection.movieRateByUser(Integer.parseInt(id), username);
            context.json(rating);
        }
    };

    public Handler checkIsMovieInWatchlist = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            String username = context.req.getParameter("name");
            boolean inWatchlist = userConnection.movieInUsersWatchlist(Integer.parseInt(id), username);
            context.json(inWatchlist);
        }
    };

    public Handler getUserList = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String username = context.req.getParameter("name");
            String token = context.req.getParameter("token");
            if (jwt.verifyJWT(token)) {
                List<MoviesResForm> movies = userConnection.getUserList(username);
                context.json(movies);
            } else {
                Map<String, String> res = new HashMap<>();
                res.put("message", "Session expired");
                res.put("status", "309");
                context.json(res);
            }
        }
    };

    public Handler deleteFromList = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            String username = context.req.getParameter("name");
            boolean isDeleted = userConnection.deleteFromList(Integer.parseInt(id), username);
            setResponse(isDeleted, context);
        }
    };

    public Handler addMovieToList = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            String username = context.req.getParameter("name");
            boolean isAdded = userConnection.addToUserList(Integer.parseInt(id), username);
            setResponse(isAdded, context);
        }
    };

    public Handler addRateToMovie = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            String username = context.req.getParameter("name");
            String rate = context.req.getParameter("rate");
            boolean isAdded = userConnection.addRateToMovie(Integer.parseInt(id), Integer.parseInt(rate), username);
            setResponse(isAdded, context);
        }
    };

    public Handler addComment = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            JavalinJackson jackson = new JavalinJackson();
            Comment comment;
            try {
                comment = jackson.fromJsonString(context.body(), Comment.class);
                userConnection.addComment(comment);
                List<Comment> comments = userConnection.getMovieComments(comment.getId());
                context.json(comments);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    };


    public Handler getComments = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            String id = context.req.getParameter("id");
            List<Comment> comments = userConnection.getMovieComments(Integer.parseInt(id));
            context.json(comments);
        }
    };

    public Handler getMostPopularMovies = new Handler() {
        @Override
        public void handle(@NotNull Context context) throws Exception {
            context.header(AccessControl, allowedPort);
            List<MoviesResForm> movies = components.getPopularMovies();
            context.json(movies);
        }
    };

    public Handler getHighRatedMovies = new Handler() {
        @Override
        public void handle(@NotNull Context context) {
            context.header(AccessControl, allowedPort);
            List<MoviesResForm> movies = components.getTopRatedMovies();
            context.json(movies);
        }
    };

    public Handler setMovieStatus = new Handler() {
        @Override
        public void handle(@NotNull Context context) throws Exception {
            context.header(AccessControl, allowedPort);
            String status = context.req.getParameter("status");
            String id = context.req.getParameter("id");
            String username = context.req.getParameter("username");
            int statusInt = Integer.parseInt(status);
            if (statusInt == 0) {
                userConnection.setMovieStatusToZero(statusInt, Integer.parseInt(id), username);
            } else {
                userConnection.setMovieStatusToOne(statusInt, Integer.parseInt(id));
            }

        }
    };
}
