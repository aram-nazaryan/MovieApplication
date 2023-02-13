package movie.components;

import java.util.List;

public class Movie {
    private final int id;
    private final String name;
    private final String url;
    private final int dir_id;
    private final String dir_name;
    private final String mov_desc;
    private final int mov_year;
    private List<Genre> genres;
    private List<Actor> actors;
    private double rating;

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getRating() {
        return rating;
    }

    public Movie(int id, String name, String url, String dir_name, int dir_id, int mov_year, String mov_desc) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.dir_name = dir_name;
        this.dir_id = dir_id;
        this.mov_desc = mov_desc;
        this.mov_year = mov_year;
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

    public int getDir_id() {
        return dir_id;
    }

    public String getDir_name() {
        return dir_name;
    }

    public List<Genre> getGenres() {
        return genres;
    }


    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public void setActors(List<Actor> actors) {
        this.actors = actors;
    }

    public String getMov_desc() {
        return mov_desc;
    }

    public int getMov_year() {
        return mov_year;
    }

    public List<Actor> getActors() {
        return actors;
    }


}
