package responses;

import java.util.List;

public class ActorResForm {
    private final int id;
    private final String name;
    private final String country;
    private final String date;
    private final String url;
    private List<MoviesResForm> movies;

    public ActorResForm(int id, String name, String country, String date, String url) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.country = country;
        this.url = url;
    }

    public void setMovies(List<MoviesResForm> movies) {
        this.movies = movies;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public String getDate() {
        return date;
    }

    public String getUrl() {
        return url;
    }

    public List<MoviesResForm> getMovies() {
        return movies;
    }
}
