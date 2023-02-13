package responses;

public class MoviesResForm {
    private final int id;
    private final String name;
    private final String url;

    private int watchListStatus = 0;
    double rating;

    public MoviesResForm(int id, String name, String url) {
        this.id = id;
        this.name = name;
        this.url = url;
    }

    public int getWatchListStatus() {
        return watchListStatus;
    }

    public void setWatchListStatus(int watchListStatus) {
        this.watchListStatus = watchListStatus;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
