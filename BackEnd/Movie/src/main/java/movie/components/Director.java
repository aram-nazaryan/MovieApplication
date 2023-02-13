package movie.components;

public class Director {
    private final int id;
    private final String name;
    private final String url;

    public Director(int id, String name, String url) {
        this.id = id;
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


