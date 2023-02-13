package movie.components;

public class Actor {
    private final int id;
    private final String name;
    private final String url;

    public Actor(int ac_id, String name, String url) {
        this.id = ac_id;
        this.name = name;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    public String getUrl() {
        return url;
    }
}



