package movie.production;

import responses.ActorResForm;
import responses.DirectorResFrom;
import responses.MoviesResForm;
import movie.components.Actor;
import movie.components.Director;
import movie.components.Genre;
import movie.components.Movie;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CombinedMovieComponents implements Helper {

    protected static CombinedMovieComponents components;
    protected static Connection conn;

    public static CombinedMovieComponents getInstance(String name, String passwd) {
        if (components == null) {
            components = new CombinedMovieComponents();
        }
        setConnection(name, passwd);
        return components;
    }

    private static void setConnection(String name, String passwd) {
        StringBuilder url = new StringBuilder();
        url.append("jdbc:postgresql://10.212.20.48:5433/aram?user=").append(name).append("&password=").append(passwd);
        try {
            conn = DriverManager.getConnection(url.toString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Movie getOneMovie(int id) {
        Movie movie;
        String query = "select movies.*, directors.dir_name, directors.dir_id from movies join directors ON directors.dir_id = movies.director where movies.mov_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int mov_id = resultSet.getInt("mov_id");
            String name = resultSet.getString("mov_name");
            String url = resultSet.getString("mov_url");
            String dir_name = resultSet.getString("dir_name");
            int year = resultSet.getInt("mov_year");
            int dir_id = resultSet.getInt("dir_id");
            String mov_desc = resultSet.getString("mov_desc");
            movie = new Movie(mov_id, name, url, dir_name, dir_id, year, mov_desc);
            movie.setGenres(getGenresForMovie(movie.getId()));
            movie.setActors(getActorsForMovie(movie.getId()));
            movie.setRating(getMovieRate(mov_id));
            return movie;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ActorResForm getOneActor(int id) {
        String query = "select * from actors where ac_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            ActorResForm form = (ActorResForm) getActorFromSet(resultSet, ActorResForm.class);
            form.setMovies(getMoviesByID(id, 0));
            return form;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public DirectorResFrom getOneDirector(int id) {
        String query = "select * from directors where dir_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            DirectorResFrom form = (DirectorResFrom) getDirectorFromSet(resultSet, DirectorResFrom.class);
            form.setMovies(getMoviesByID(id, 1));
            return form;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    private List<MoviesResForm> getMoviesByID(int id, int specifier) {
        List<MoviesResForm> movies = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            StringBuilder query = new StringBuilder();
            query.append("select movies.mov_id, movies.mov_name, movies.mov_url from movies join ");
            if (specifier == 1) {
                query.append("directors ON directors.dir_id = movies.director where directors.dir_id = ");
            } else {
                query.append("movie_actor ON movie_actor.mov_id = movies.mov_id where movie_actor.act_id = ");
            }
            query.append(id);
            ResultSet resultSet = statement.executeQuery(query.toString());
            while (resultSet.next()) {
                movies.add(getMovieFromSet(resultSet));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    private double getMovieRate(int id) {
        String query = "select movies.mov_name,  AVG(rating) as med from rateing join movies ON movies.mov_id = rateing.mov_id where rateing.mov_id = ?  group by movies.mov_name";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet getRate = statement.executeQuery();
            getRate.next();
            double rate = getRate.getDouble("med");
            return (double) Math.round(rate * 10) / 10;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0.0;
    }

    public List<MoviesResForm> getAllMovies() {
        List<MoviesResForm> movies = new ArrayList<>();
        String query = "select mov_id, mov_name, mov_url from movies";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MoviesResForm form = getMovieFromSet(resultSet);
                int mov_id = resultSet.getInt("mov_id");
                form.setRating(getMovieRate(mov_id));
                movies.add(form);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    private List<Genre> getGenresForMovie(int id) {
        List<Genre> genres = new ArrayList<>();
        String query = "select geners.gen_name from geners join movie_gener on movie_gener.gen_id = geners.gen_id where movie_gener.mov_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String gen = resultSet.getString("gen_name");
                genres.add(Genre.getGenre(gen));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return genres;
    }

    private List<Actor> getActorsForMovie(int id) {
        List<Actor> actors = new ArrayList<>();
        String query = "select actors.* from actors join movie_actor ON movie_actor.act_id = actors.ac_id join movies ON movies.mov_id = movie_actor.mov_id where movies.mov_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Actor actor = (Actor) getActorFromSet(resultSet, Actor.class);
                actors.add(actor);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return actors;
    }

    public List<Actor> getActors() {
        List<Actor> actors = new ArrayList<>();
        String query = "select * from actors ";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Actor actor = (Actor) getActorFromSet(resultSet, Actor.class);
                actors.add(actor);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return actors;
    }


    public List<Actor> getActors(Map<String, String> params) {
        List<Actor> actors = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(createExecutableQuery(params, "actors"));
            while (resultSet.next()) {
                Actor actor = (Actor) getActorFromSet(resultSet, Actor.class);
                actors.add(actor);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return actors;
    }


    public List<Director> getDirectors() {
        List<Director> directors = new ArrayList<>();
        String query = "select * from directors";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Director director = (Director) getDirectorFromSet(resultSet, Director.class);
                directors.add(director);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return directors;
    }

    public List<Director> getDirectors(Map<String, String> params) {
        List<Director> directors = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(createExecutableQuery(params, "directors"));
            while (resultSet.next()) {
                Director director = (Director) getDirectorFromSet(resultSet, Director.class);
                directors.add(director);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return directors;
    }

    public List<MoviesResForm> getMoviesByParams(Map<String, String> params) {
        List<MoviesResForm> movies = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
            ResultSet resultSet = statement.executeQuery(createExecutableQueryMovie(params));
            while (resultSet.next()) {
                MoviesResForm form = getMovieFromSet(resultSet);
                int mov_id = resultSet.getInt("mov_id");
                form.setRating(getMovieRate(mov_id));
                movies.add(form);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    public List<MoviesResForm> getPopularMovies() {
        List<MoviesResForm> movies = new ArrayList<>();
        String query = """
                select m.mov_id, m.mov_name, m.mov_url, count(watchlist.mov_id) as c\s
                from watchlist\s
                    join movies m on m.mov_id = watchlist.mov_id\s
                group by m.mov_id\s
                order by c desc limit 5;""";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MoviesResForm movie = getMovieFromSet(resultSet);
                movies.add(movie);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }


    public List<MoviesResForm> getTopRatedMovies() {
        List<MoviesResForm> movies = new ArrayList<>();
        String query = """
                select m.mov_id, m.mov_name, m.mov_url, avg(rating) rate
                from movies m
                join rateing r on m.mov_id = r.mov_id
                group by m.mov_id
                order by rate desc
                limit 5
                """;
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MoviesResForm movie = getMovieFromSet(resultSet);
                movies.add(movie);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    public List<MoviesResForm> getMoviesByName(String name) {
        List<MoviesResForm> movies = new ArrayList<>();
        String query = """
                select mov_id, mov_name, mov_url
                from movies
                where lower(mov_name) like lower(?);
                """;
        String movieName = "%" + name + "%";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, movieName);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MoviesResForm movie = getMovieFromSet(resultSet);
                int mov_id = resultSet.getInt("mov_id");
                movie.setRating(getMovieRate(mov_id));
                movies.add(movie);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }
}
