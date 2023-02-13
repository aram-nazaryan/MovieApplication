package org.example;

import movie.components.Comment;
import controller.RequestController;
import movie.production.CombinedMovieComponents;
import movie.production.UserConnection;
import io.github.cdimascio.dotenv.Dotenv;
import io.javalin.Javalin;
import io.javalin.websocket.WsContext;

import java.util.ArrayList;
import java.util.List;


public class App {
    public static void main(String[] args) {

        Dotenv dotenv = Dotenv.configure().load();
        String password = dotenv.get("PASSWORD_LOCAL_DB");
        String username = dotenv.get("USERNAME_LOCAL_DB");
        CombinedMovieComponents components = CombinedMovieComponents.getInstance(username, password);
        UserConnection connection = UserConnection.getInstance();
        try {
            RequestController controller = RequestController.getInstance(components);
            try {
                Javalin app = Javalin.create().start(5057);
                app.get("/movies", controller.getMovies);
                app.post("/test", controller.test);
                app.get("/actors", controller.getActors);
                app.get("/directors", controller.getDirectors);
                app.get("/movie", controller.getOneMovie);
                app.get("/actor", controller.getOneActor);
                app.get("/director", controller.getOneDirector);
                app.get("/user", controller.getUser);
                app.get("/filteredMovies", controller.getFilteredMovies);
                app.get("/rated", controller.checkIsMovieRated);
                app.get("/watchlist", controller.checkIsMovieInWatchlist);
                app.get("/userlist", controller.getUserList);
                app.get("/comments", controller.getComments);
                app.get("/popularmovies", controller.getMostPopularMovies);
                app.get("/bestmovies", controller.getHighRatedMovies);

                app.post("/register", controller.registerUser);
                app.post("/add", controller.addMovieToList);
                app.post("/remove", controller.deleteFromList);
                app.post("/rate", controller.addRateToMovie);
                app.post("/comment", controller.addComment);
                app.post("/setstatus", controller.setMovieStatus);


                List<WsContext> connectedUsers = new ArrayList<>();
                app.ws("/chat", ws -> {
                    ws.onConnect(connectedUsers::add);

                    ws.onMessage(context -> {
                        Comment comment = context.messageAsClass(Comment.class);
                        connection.addComment(comment);
                        List<Comment> comments = connection.getMovieComments(comment.getId());
                        for (WsContext connectedUser : connectedUsers) {
                            connectedUser.send(comments);
                        }
                    });

                    ws.onClose(connectedUsers::remove);
                });

            } catch (Exception e) {
                System.out.println(e.getMessage() + " app error");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage() + " controller error");
        }


    }
}
