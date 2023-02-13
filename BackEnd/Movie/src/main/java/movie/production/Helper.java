package movie.production;

import responses.ActorResForm;
import responses.DirectorResFrom;
import responses.MoviesResForm;
import movie.components.Actor;
import movie.components.Director;
import io.javalin.http.Context;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public interface Helper {
    default String createExecutableQuery(Map<String, String> params, String specifier) {
        List<String> query = new ArrayList<>();
        if (params.containsKey("name"))
            query.add(" lower(" + (specifier.equals("actors") ? "ac_name)" : "dir_name)") + " like '%" + params.get("name") + "%' ");
        if (params.containsKey("country"))
            query.add((specifier.equals("actors") ? " ac_country = '" : " dir_country = '") + params.get("country") + "' ");
        if (params.containsKey("gender"))
            query.add(" gender = '" + params.get("gender") + "' ");
        if (params.containsKey("year"))
            query.add(" ac_date " + ((params.get("yearCond")).equals("bef") ? "<" : ">=") + "'" + params.get("year") + "-01-01' ");

        StringBuilder executableQuery = new StringBuilder();
        executableQuery.append("select * from ").append(specifier).append(" where ");
        for (int i = 0; i < query.size(); ++i) {
            executableQuery.append(query.get(i));
            if (i != query.size() - 1)
                executableQuery.append(" and");
        }
        return executableQuery.toString();
    }


    default String createExecutableQueryMovie(Map<String, String> params) {
        List<String> query = new ArrayList<>();
        StringBuilder executableQuery = new StringBuilder();
        executableQuery.append("""
                select movies.*, AVG(rateing.rating)
                from movies join movie_gener ON movie_gener.mov_id = movies.mov_id
                join geners ON geners.gen_id = movie_gener.gen_id full
                join rateing ON rateing.mov_id = movies.mov_id""");

        boolean flag = false;
        if (params.containsKey("celebrity")) {
            if (params.get("celebrity").equals("director")) {
                executableQuery.append(" join directors ON directors.dir_id = movies.director where ");
                if (params.containsKey("name"))
                    query.add(" lower(directors.dir_name) like '%" + params.get("name") + "%' ");
            } else if (params.get("celebrity").equals("actor")) {
                executableQuery.append(" join movie_actor ON movie_actor.mov_id = movies.mov_id join actors ON actors.ac_id = movie_actor.act_id where ");//asdsa
                if (params.containsKey("name"))
                    query.add(" lower(actors.ac_name) like '%" + params.get("name") + "%' ");
            }
            flag = true;
        }


        if (params.containsKey("year"))
            query.add(" movies.mov_year " + ((params.get("cond")).equals("bef") ? "<" : ">=") + params.get("year"));
        if (params.containsKey("genre"))
            query.add(" geners.gen_name = '" + params.get("genre") + "'");
        if (query.size() != 0 && !flag)
            executableQuery.append(" where ");
        for (int i = 0; i < query.size(); ++i) {
            executableQuery.append(query.get(i));
            if (i != query.size() - 1)
                executableQuery.append(" and ");
        }
        executableQuery.append(" group by movies.mov_id ").append(" order by avg ");
        if (params.containsKey("rate"))
            executableQuery.append(params.get("rate").equals("hf") ? " desc " : "");
        return executableQuery.toString();
    }

    default MoviesResForm getMovieFromSet(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("mov_id");
        String name = resultSet.getString("mov_name");
        String url = resultSet.getString("mov_url");
        //int status = resultSet.getInt("open_state");
        MoviesResForm movie = new MoviesResForm(id, name, url);
        //movie.setWatchListStatus(status);
        return movie;
    }

    default Object getActorFromSet(ResultSet resultSet, Object type) throws SQLException {
        int id = resultSet.getInt("ac_id");
        String name = resultSet.getString("ac_name");
        String url = resultSet.getString("url_pic");
        String date = resultSet.getString("ac_date");
        String country = resultSet.getString("ac_country");
        //char gender = resultSet.getString("gender").charAt(0);
        if (((Class) type).getName().equals(Actor.class.getName())) {
            return (new Actor(id, name, url));
        } else {
            return (new ActorResForm(id, name, country, date, url));
        }

    }

    default Object getDirectorFromSet(ResultSet resultSet, Object type) throws SQLException {
        int id = resultSet.getInt("dir_id");
        String name = resultSet.getString("dir_name");
        String url = resultSet.getString("url_pic");
        String date = resultSet.getString("ac_date");
        String country = resultSet.getString("dir_country");
        //char gender = resultSet.getString("gender").charAt(0);
        if (((Class) type).getName().equals(Director.class.getName())) {
            return (new Director(id, name, url));
        } else {
            return (new DirectorResFrom(id, name, date, country, url));
        }
    }

    default Map<String, String> getParams(String name, String country, String gender, String year, String yearCond) {
        Map<String, String> params = new HashMap<>();
        if (name != null)
            params.put("name", name);
        if (country != null)
            params.put("country", country);
        if (gender != null)
            params.put("gender", gender);
        if (year != null) {
            params.put("year", year);
            params.put("yearCond", yearCond);
        }
        return params;
    }

    default Map<String, String> movieParamList(String genre, String rate, String year, String cond, String celebrity, String name) {
        Map<String, String> params = new HashMap<>();
        if (genre != null)
            params.put("genre", genre);
        if (rate != null)
            params.put("rate", rate);
        if (year != null)
            params.put("year", year);
        if (cond != null)
            params.put("cond", cond);
        if (celebrity != null)
            params.put("celebrity", celebrity);
        if (name != null)
            params.put("name", name);
        return params;
    }

    default void setResponse(boolean successful, Context context) {
        if (successful) {
            context.res.setStatus(200);
            context.result("ok");
        } else {
            context.res.setStatus(330);
            context.result("failed to add");
        }
    }
}
