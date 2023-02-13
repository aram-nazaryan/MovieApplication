package movie.components;

public enum Genre {
    Action,
    Adventure,
    Comedy,
    Crime,
    Fantasy,
    Historical,
    Romance,
    Thriller,
    Drama,
    Biography,
    Musical;

    public static Genre getGenre(String gen) throws Exception {
        for (Genre value : Genre.values()) {
            if (value.name().equals(gen))
                return value;
        }
        throw new Exception("Incorrect Genre.");
    }

}
