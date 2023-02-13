package movie.production;

import movie.components.Comment;
import responses.MoviesResForm;
import responses.UserRegisterForm;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserConnection extends CombinedMovieComponents {

    private static UserConnection connection;

    public static UserConnection getInstance() {
        if (connection == null) {
            connection = new UserConnection();
        }
        return connection;
    }

    public boolean userExists(String username) {
        String query = "select * from users where username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean matchPassword(String username, String password) {
        String query = "select * from users where username = ? and password = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void addUser(UserRegisterForm user) { // should be modified
        String query = "insert into users (username, password) VALUES(?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public int movieRateByUser(int id, String username) {
        String query = "select rateing.rating from rateing join users ON users.id = rateing.usr_id where users.username = ? and rateing.mov_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("rating");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean movieInUsersWatchlist(int id, String username) {
        String query = "select * from watchlist join users ON users.id = watchlist.usr_id where users.username = ? and watchlist.mov_id = ? ";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setInt(2, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    private int findStatus(int id, int user_id) {
        String query = "select open_state from watchlist where mov_id = ? and usr_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, user_id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("open_state");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public List<MoviesResForm> getUserList(String username) {
        List<MoviesResForm> movies = new ArrayList<>();
        int usr_id = getUserID(username);
        String query = "select movies.mov_id, movies.mov_name, movies.mov_url from watchlist join users ON users.id = watchlist.usr_id join movies ON movies.mov_id = watchlist.mov_id where users.username = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                MoviesResForm form = getMovieFromSet(resultSet);
                form.setWatchListStatus(findStatus(form.getId(), usr_id));
                movies.add(form);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return movies;
    }

    public int getUserID(String username) {
        String query = "select id from users where username = ? ";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt("id");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public boolean deleteFromList(int id, String username) {
        String query = "delete from watchlist where mov_id = ? and usr_id = ? ";
        int usrId = getUserID(username);
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, usrId);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addToUserList(int id, String username) {
        String query = "insert into watchlist values (?, ?)";
        int usrID = getUserID(username);
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, usrID);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean addRateToMovie(int id, int rate, String username) {
        String query = "insert into rateing values (?, ?, ?); ";
        int usrID = getUserID(username);
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.setInt(2, usrID);
            statement.setInt(3, rate);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public void addComment(Comment comment) {
        String query = "insert into review(mov_id, usr_id, comment) values (?, ?, ?)";
        int usrID = getUserID(comment.getUsername());
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, comment.getId());
            statement.setInt(2, usrID);
            statement.setString(3, comment.getComment());
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Comment> getMovieComments(int id) {
        String query = "select review.mov_id, users.username, review.comment  from review join users ON users.id = review.usr_id where review.mov_id = ? order by review.id desc";
        List<Comment> comments = new ArrayList<>();
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int mov_id = resultSet.getInt("mov_id");
                String username = resultSet.getString("username");
                String comment = resultSet.getString("comment");
                comments.add(new Comment(mov_id, comment, username));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return comments;
    }

    public void setMovieStatusToOne(int status, int mov_id) {
        String query = "update watchlist set open_state = ? where mov_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, status);
            statement.setInt(2, mov_id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void setMovieStatusToZero(int status, int mov_id, String username) {
        String query = "update watchlist set open_state = ? where mov_id = ? and usr_id = ?";
        int user_id = getUserID(username);
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, status);
            statement.setInt(2, mov_id);
            statement.setInt(3, user_id);
            statement.executeUpdate();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
