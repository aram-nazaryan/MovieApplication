package movie.components;

public class Comment {
    private int id;
    private String comment;
    private String username;

    public Comment() {

    }

    public Comment(int id, String comment, String username) {
        this.id = id;
        this.comment = comment;
        this.username = username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getId() {
        return id;
    }

    public String getComment() {
        return comment;
    }

    public String getUsername() {
        return username;
    }
}
