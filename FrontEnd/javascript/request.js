export const ip = "http://10.212.20.48:5050/"

export async function get_movies() {
    const result = await fetch(`${ip}movies`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_most_popular_movies() {
    const result = await fetch(`${ip}popularmovies`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_movies_by_name(name) {
    const result = await fetch(`${ip}movies?name=${name}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}



export async function get_highest_movies() {
    const result = await fetch(`${ip}bestmovies`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_movie(id) {
    const result = await fetch(`${ip}movie?id=${id}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function getComments(id) {
    const result = await fetch(`${ip}comments?id=${id}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_actors() {
    const result = await fetch(`${ip}actors`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_directors() {
    const result = await fetch(`${ip}directors`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_one_actor(id) {
    const result = await fetch(`${ip}actor?id=${id}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_one_director(id) {
    const result = await fetch(`${ip}director?id=${id}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}


export async function filter_celebrity(attributes) {
    if (attributes[0][1] === "directors") {
        return filter_directors(attributes);
    } else {
        return filter_actors(attributes)
    }

}

export async function filter_films(attributes) {
    let url = `${ip}filteredMovies?`;
    url += creatURLMovies(attributes);
    const result = await fetch(url, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

function creatURLMovies(attributes) {
    let url = "";
    for (let i = 0; i < attributes.length; ++i) {
        if (attributes[i][0] === 1) {
            url += "genre=" + attributes[i][1];
        } else if (attributes[i][0] === 2) {
            url += "rate=" + attributes[i][1];
        } else if (attributes[i][0] === 3) {
            url += "year=" + attributes[i][1][1] + "&cond=" + attributes[i][1][0];
        } else if (attributes[i][0] === 4) {
            url += "cel=" + attributes[i][1][0] + "&name=" + attributes[i][1][1].toLowerCase();
        }
        if (i != attributes.length - 1) {
            url += '&';
        }
    }
    return url;
}


function creatURL(attributes) {
    let url = "";
    for (let i = 1; i < attributes.length; ++i) {

        if (attributes[i][0] === 2) {
            url += "name=" + attributes[i][1].toLowerCase();
        }
        if (attributes[i][0] === 3) {
            url += "year=" + attributes[i][1][1] + "&cond=" + attributes[i][1][0];
        }
        if (attributes[i][0] === 4) {
            url += "gender=" + attributes[i][1];
        }
        if (attributes[i][0] === 5) {
            url += "country=" + attributes[i][1];
        }
        if (i != attributes.length - 1)
            url += '&';
    }
    return url;
}


async function filter_actors(attributes) {
    let url = `${ip}actors?`;
    url += creatURL(attributes);
    const result = await fetch(url, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

async function filter_directors(attributes) {

    let url = `${ip}directors?`;
    url += creatURL(attributes);
    const result = await fetch(url, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function user_registerPOST(obj) {
    const result = await fetch(`${ip}register`, {
        method: 'POST',
        body: JSON.stringify(obj)
    });

    const parsObj = await result.json();

    return parsObj;

}

export async function removeMovie(id) {
    const result = await fetch(`${ip}remove?id=${id}&name=${sessionStorage.getItem("name")}`, { method: 'POST' });

    const parsObj = await result;

    return parsObj;
}

export async function addMovie(id) {
    const result = await fetch(`${ip}add?id=${id}&name=${sessionStorage.getItem("name")}`, { method: 'POST' });

    const parsObj = await result;

    return parsObj;
}

export async function addRate(id, value) {
    const result = await fetch(`${ip}rate?id=${id}&rate=${value}&name=${sessionStorage.getItem("name")}`, { method: 'POST' });

    const parsObj = await result;

    return parsObj;
}


export async function addComment(obj) {


    const result = await fetch(`${ip}comment`, {
        method: 'POST',

        body: JSON.stringify(obj)
    });

    const parsObj = await result.json();

    return parsObj;
}



export async function get_user(obj) {
    const result = await fetch(`${ip}user?name=${obj[0]}&pass=${obj[1]}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}


export async function check_movie_rated(id) {
    const result = await fetch(`${ip}rated?id=${id}&name=${sessionStorage.getItem("name")}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}


export async function check_movie_in_watchlist(id) {
    const result = await fetch(`${ip}watchlist?id=${id}&name=${sessionStorage.getItem("name")}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function get_watch_list() {
    const result = await fetch(`${ip}userlist?name=${sessionStorage.getItem("name")}&token=${sessionStorage.getItem("token")}`, { method: "GET" });

    const parsObj = await result.json();

    return parsObj;
}

export async function set_status(mov_id, status) {
    const result = await fetch(`${ip}setstatus?username=${sessionStorage.getItem("name")}&status=${status}&id=${mov_id}`, { method: "POST" });
}