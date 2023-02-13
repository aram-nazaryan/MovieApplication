import {
    movieFilterPart,
    AC_DIR_FilterPart,
    find_button_actor,
    find_button_dir,
    mainPageCont,
    addInfoSellActor,
    addInfoSellDirector,
    addInfoSellMovie,
    addInfoSellMovieRate,
    addCelebMainPage,
    login_page,
    addMovieMainPage,
    modal,
    user_comment,
    found_fav,
    addInfoSellMovieForList,
    user_comment1,
    movie_panel
} from './render.js'

import {
    get_movies,
    get_actors,
    get_directors,
    filter_celebrity,
    get_one_actor,
    get_one_director,
    user_registerPOST,
    get_user,
    filter_films,
    get_movie,
    check_movie_in_watchlist,
    check_movie_rated,
    get_watch_list,
    removeMovie,
    addMovie,
    addRate,
    addComment,
    getComments,
    ip,
    get_most_popular_movies,
    get_highest_movies,
    get_movies_by_name,
    set_status
} from './request.js'


const content = document.getElementById("content");


window.onload = function onLoadFunc() {
    if (sessionStorage.getItem("name") != null && sessionStorage.getItem("token") != null) {
        log_out.style.display = "block";
        login.style.display = "none";
        fav.style.display = "block";
        username_lg.innerHTML = sessionStorage.getItem("name")
        userlogo.style.display = 'block'
        watchlistpanel.innerHTML = `<div class="wtchpanel">CHATS</div>`
        let movies = get_watch_list();
        movies.then(data => data.forEach(movie => {
            watchlistpanel.innerHTML += movie_panel(movie)
            //check status and make display of icon block
            if (movie.watchListStatus != 0) {
                Array.from(message_icon_chat).forEach(mov => {
                        if (movie.id == mov.id) {
                            mov.style.display = 'block'
                        }
                    }
                )
            }
        }))
    } else {
        userlogo.style.display = 'none'
    }
    home_btn.classList.add("aj_btn");
    content.innerHTML += mainPageCont()
    let popMovies = get_most_popular_movies();
    popMovies.then(data => data.forEach(movie => popularMovies.innerHTML += addInfoSellMovie(movie)));
    let topMovies = get_highest_movies();
    topMovies.then(data => data.forEach(movie => highratedMovies.innerHTML += addInfoSellMovie(movie)))

    if (location.pathname.includes("actors")) {
        actorsonload();

    } else if (location.pathname.includes("director")) {
        directors();

    } else if (location.pathname.includes("film")) {
        filmsonload()

    } else if (location.pathname.includes("watchlist")) {

        favsonload();

    } else if (location.pathname.includes("login")) {
        loginonload()
    } else {
        window.history.pushState(null, null, '/')
    }

    // is used for arfter refreshing page
    //console.log(location.pathname)

}

window.clearFileds = () => {
    mianSearch.value = ""
    genre.value = "";
    rating.value = "";
    year.value = "";
    celebrity.value = "";
    year_val.value = "";
    celebrity_value.value = "";

}

window.clearFiledsCel = () => {
    mianSearch.value = ""
    gender.value = "";
    year.value = "";
    country.value = "";
    year_val.value = "";
    celebrity_value.value = "";
}

window.actorsonload = (e) => {
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.add("aj_btn");
    directors_btn.classList.remove("aj_btn");
    fav.classList.remove("aj_btn");
    login.classList.remove("aj_btn");
    console.log(e);
    if (!(typeof (e) === 'undefined') && e != null) {
        console.log(e)
        content.innerHTML = addCelebMainPage(e)
        e.movies.forEach(movie => films.innerHTML += addInfoSellMovie(movie))

    } else {
        //loadActors()
        content.innerHTML = AC_DIR_FilterPart() + find_button_actor();
        let actors = get_actors();
        actors.then(data => data.forEach(actor => found_films.innerHTML += addInfoSellActor(actor)));
    }
}

window.directors = (e) => {
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.remove("aj_btn");
    directors_btn.classList.add("aj_btn");
    fav.classList.remove("aj_btn");
    login.classList.remove("aj_btn");
    if (!(typeof (e) === 'undefined') && e != null) {
        console.log(e)
        content.innerHTML = addCelebMainPage(e)
        e.movies.forEach(movie => films.innerHTML += addInfoSellMovie(movie))
    } else {
        //loadDirectors()
        content.innerHTML = AC_DIR_FilterPart() + find_button_dir();
        let directors = get_directors();
        directors.then(data => data.forEach(director => found_films.innerHTML += addInfoSellDirector(director)))
    }
}

window.filmsonload = (e) => {
    if (!(typeof (e) === 'undefined') && e != null) {
        home_btn.classList.remove("aj_btn");
        films_btn.classList.add("aj_btn");
        actors_btn.classList.remove("aj_btn");
        directors_btn.classList.remove("aj_btn");
        fav.classList.remove("aj_btn");
        login.classList.remove("aj_btn");
        goToMovie(e.id, 1);

    } else {
        goToFilms("", 1)
    }
}

window.favsonload = () => {
    login.classList.remove("aj_btn");
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.remove("aj_btn");
    fav.classList.add("aj_btn");
    directors_btn.classList.remove("aj_btn");
    let userlist = get_watch_list();
    content.innerHTML = found_fav();
    userlist.then(data => {
        if (data.length != 0) {
            data.forEach(movie => found_fav_films.innerHTML += addInfoSellMovieForList(movie))
        } else {
            found_fav_films.innerHTML += "Your WatchList is empty."
        }
    })
}

window.loginonload = () => {
    login.classList.add("aj_btn");
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.remove("aj_btn");
    fav.classList.remove("aj_btn");
    directors_btn.classList.remove("aj_btn");
    content.innerHTML = login_page();
}

window.addEventListener("popstate", e => {
    console.log(e, "<-e\n", location)

    if (location.pathname.includes("actors")) {
        actorsonload(e.state);

    } else if (location.pathname.includes("director")) {
        directors(e.state);

    } else if (location.pathname.includes("film")) {
        filmsonload(e.state)

    } else if (location.pathname.includes("watchlist")) {

        favsonload();

    } else if (location.pathname.includes("login")) {
        loginonload()
    } else {
        location.reload();
    }
})

window.goToActorInfo = (id) => {
    let actor = get_one_actor(id);
    actor.then(data => {
        window.history.pushState(data, null, `/actors/${data.name}`)
        content.innerHTML = addCelebMainPage(data)
    })
    actor.then(data => data.movies.forEach(movie => films.innerHTML += addInfoSellMovie(movie)));
}

window.goToDirectorInfo = (id) => {
    let director = get_one_director(id);
    director.then(data => {
        window.history.pushState(data, null, `/directors/${data.name}`)
        content.innerHTML = addCelebMainPage(data)
    })
    director.then(data => data.movies.forEach(movie => films.innerHTML += addInfoSellMovie(movie)));
}

window.loadMainPage = () => {
    window.history.pushState(null, null, '/')
    location.reload();
}

window.remFromWatchlist = (id, obj) => {
    removeMovie(id);
    watchlistpanel.innerHTML = `<div class="wtchpanel">CHATS</div>`;

    if (typeof (obj) === 'undefined') {
        goToMovie(id, 1);
    } else {
        obj.parentElement.remove();
    }
    let movies = get_watch_list();
    movies.then(data => data.forEach(movie => {
        watchlistpanel.innerHTML += movie_panel(movie)
    }))
}

window.addToWatchlist = (id) => {
    addMovie(id);
    goToMovie(id, 1);
    watchlistpanel.innerHTML = `<div class="wtchpanel">CHATS</div>`;
    let movies = get_watch_list();
    movies.then(data => data.forEach(movie => watchlistpanel.innerHTML += movie_panel(movie)))
}

window.addRate = (id) => {
    addRate(id, rate.value);
    goToMovie(id, 1);
}

let currentMoviePage = 0;

window.goToMovie = (id, flag) => {
    currentMoviePage = id;
    let movie = get_movie(id);
    movie.then(data => {
        if (flag === 0) {
            window.history.pushState(data, null, `/films/${data.name}`)
        }
        content.innerHTML = addMovieMainPage(data);
        if (sessionStorage.getItem("name") != null) {
            let watchlist = check_movie_in_watchlist(data.id);
            watchlist.then(wch => {
                if (wch) {
                    movieInfo.innerHTML += `<button  id="remwatchlist" onclick="remFromWatchlist(${data.id})">Remove From List</button> `
                } else {
                    movieInfo.innerHTML += `<button  id="watchlist" onclick="addToWatchlist(${data.id})">Add to Watchlist</button>`
                }
            })

            let rate = check_movie_rated(data.id);
            rate.then(rt => {
                if (rt != -1) {
                    ratingDiv.innerHTML += `<br>Your choice: ${rt}`
                } else {
                    ratingDiv.innerHTML += `<br><select id="rate">
                    <option value="">Your Rate</option>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                    <option value="6">6</option>
                    <option value="7">7</option>
                    <option value="8">8</option>
                    <option value="9">9</option>
                    <option value="10">10</option>
                </select>
                <button  id="addRate" onclick="addRate(${data.id})">Rate</button>`
                }
            })
        }
    })
    movie.then(data => data.actors.forEach(actor => films.innerHTML += addInfoSellActor(actor)));

}

window.goToFilms = (inputLine, flag) => {
    if (flag === 0) {
        window.history.pushState(null, null, '/films')
    }
    home_btn.classList.remove("aj_btn");
    films_btn.classList.add("aj_btn");
    actors_btn.classList.remove("aj_btn");
    directors_btn.classList.remove("aj_btn");
    fav.classList.remove("aj_btn");
    login.classList.remove("aj_btn");
    content.innerHTML = movieFilterPart();
    if (inputLine != "" || typeof (inputLine) === 'undefined') {
        let movies_by_name = get_movies_by_name(inputLine);
        movies_by_name.then(data => {
            if (data.length != 0) {
                data.forEach(movie => movies_with_rate.innerHTML += addInfoSellMovieRate(movie))
            } else {
                movies_with_rate.innerHTML += "Sorry, no such film :("
            }
        })
    } else {
        let movies = get_movies();
        movies.then(data => {
            if (data.length != 0) {
                data.forEach(movie => movies_with_rate.innerHTML += addInfoSellMovieRate(movie))
            } else {
                movies_with_rate.innerHTML += "Sorry, no such film :("
            }
        })//data.forEach(movie => movies_with_rate.innerHTML += addInfoSellMovieRate(movie)));
    }
}

window.loadActors = () => {
    window.history.pushState(null, null, '/actors')
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.add("aj_btn");
    directors_btn.classList.remove("aj_btn");
    fav.classList.remove("aj_btn");
    login.classList.remove("aj_btn");
    content.innerHTML = AC_DIR_FilterPart() + find_button_actor();
    let actors = get_actors();
    actors.then(data => data.forEach(actor => found_films.innerHTML += addInfoSellActor(actor)));
}

window.loadDirectors = () => {
    window.history.pushState(null, null, '/directors')
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.remove("aj_btn");
    directors_btn.classList.add("aj_btn");
    fav.classList.remove("aj_btn");
    login.classList.remove("aj_btn");
    content.innerHTML = AC_DIR_FilterPart() + find_button_dir();
    let directors = get_directors();
    directors.then(data => data.forEach(director => found_films.innerHTML += addInfoSellDirector(director)))
}


window.loadFilms = () => {
    let attributes = [];

    if (genre.value) {
        attributes.push([1, genre.value]);
    }
    if (rating.value) {
        attributes.push([2, rating.value]);
    }
    if (year.value && year_val.value) {
        attributes.push([3, [
            year.value,
            year_val.value
        ]])
    }
    if (celebrity.value && celebrity_value.value) {
        attributes.push([4, [
            celebrity.value,
            celebrity_value.value
        ]])
    }
    let films = filter_films(attributes)
    movies_with_rate.innerHTML = "";
    films.then(data => (data.forEach(film => movies_with_rate.innerHTML += addInfoSellMovieRate(film))));
}


window.loadActor = () => {
    let attributes = [];
    attributes.push([1, 'actors'])
    loadCelebrity(attributes)
}

window.loadDir = () => {
    let attributes = [];
    attributes.push([1, 'directors'])
    loadCelebrity(attributes)
}

window.loadCelebrity = (attributes) => {
    if (celebrity_value.value) {
        attributes.push([2, celebrity_value.value]);
    }
    if (year.value && year_val.value) {
        attributes.push([3, [
            year.value,
            year_val.value
        ]])
    }
    if (gender.value) {
        attributes.push([4, gender.value]);
    }
    if (country.value) {
        attributes.push([5, country.value]);
    }
    let celebrity = filter_celebrity(attributes);
    found_films.innerHTML = "";
    if (attributes[0][1] === "directors") {
        celebrity.then(data => data.forEach(cel => found_films.innerHTML += addInfoSellDirector(cel)));
    } else {
        celebrity.then(data => data.forEach(cel => found_films.innerHTML += addInfoSellActor(cel)));
    }

}


window.loadLoginPage = () => {
    window.history.pushState(null, null, '/login')
    login.classList.add("aj_btn");
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.remove("aj_btn");
    fav.classList.remove("aj_btn");
    directors_btn.classList.remove("aj_btn");
    content.innerHTML = login_page();
}

window.loadLogOut = () => {
    window.history.pushState(null, null, '/')
    log_out.style.display = "none";
    login.style.display = "block";
    fav.style.display = "none";
    sessionStorage.removeItem("name");
    sessionStorage.removeItem("token");
    window.location.reload()
}

window.whiteBorder = (obj) => {
    if (obj.value != "") {
        reg_username.style.border = 'none';
        reg_passwd.style.border = 'none'
        passwd.style.border = 'none'
        username.style.border = 'none'
    }
}

window.register_user = () => {
    if (reg_passwd.value == "") {
        reg_passwd.style.border = "2px solid red"
        return;
    }
    let reqBody = {
        "username": reg_username.value,
        "password": reg_passwd.value
    }
    let response = user_registerPOST(reqBody);
    response.then(data => {
        if (data.status === "300") {
            reg_username.style.border = "2px solid red"
        } else {
            reg_username.value = "";
            reg_passwd.value = "";
            alert("Welcome!")
        }
    });
}

window.login_user = () => {
    if (passwd.value == "") {
        passwd.style.border = "2px solid red"
        return;
    }

    let reqBody = [
        username.value,
        passwd.value
    ]

    let response = get_user(reqBody);
    response.then(data => {
        if (data.status === "310") {
            username.style.border = "2px solid red"
        } else if (data.status === "311") {
            passwd.style.border = "2px solid red"
        } else {
            sessionStorage.setItem("name", data.name);
            sessionStorage.setItem("token", data.jwt);
            window.history.pushState(null, null, '/')
            window.location.reload()
        }
    });
}

window.loadFavourite = () => {
    window.history.pushState(null, null, '/watchlist')
    login.classList.remove("aj_btn");
    home_btn.classList.remove("aj_btn");
    films_btn.classList.remove("aj_btn");
    actors_btn.classList.remove("aj_btn");
    fav.classList.add("aj_btn");
    directors_btn.classList.remove("aj_btn");
    let userlist = get_watch_list();
    content.innerHTML = found_fav();
    userlist.then(data => {
        try {
            if (data.status === '309') {
                window.history.pushState(null, null, '/login')
                loadLogOut();
            }
        } catch (e) {

        }
        if (data.length != 0) {
            data.forEach(movie => found_fav_films.innerHTML += addInfoSellMovieForList(movie))
        } else {
            found_fav_films.innerHTML += "Your WatchList is empty."
        }

    })
}

let is_modal_opend = false;
let currentModal = 0;

const message_icon_chat = document.getElementsByClassName("message_icon_chat");

window.ws = new WebSocket("ws://10.212.20.48:5050/chat");

ws.addEventListener("open", () => {
})

ws.addEventListener("message", ({data}) => {
    if (is_modal_opend) {
        com_part.innerHTML = "";
    }
    let obj = JSON.parse(data);
    obj.forEach(comment => {
        if (is_modal_opend) {
            if (comment.username === sessionStorage.getItem("name")) {
                com_part.innerHTML += user_comment1(comment)
            } else {
                com_part.innerHTML += user_comment(comment)
            }
        } else {
            if (comment.id === currentMoviePage) {
                message_icon.style.display = 'block'
            }

        }

        Array.from(message_icon_chat).forEach(mov => {
            if (comment.id == mov.id && currentModal != comment.id) {
                mov.style.display = 'block'
                //will be status set 1 function
                set_status(mov.id, 1);
            }

        })

    })
})

ws.addEventListener("close", () => {
})

let flag = true;
window.openpanel = () => {
    if (flag) {
        watchlistpanel.style.display = 'flex';
        flag = false;
    } else {
        watchlistpanel.style.display = 'none';
        flag = true;
    }

}

window.addUserComent = (id) => {

    let obj = {
        "id": id,
        "comment": comment.value,
        "username": sessionStorage.getItem("name"),
    }
    comment.value = "";
    com_part.innerHTML = "";
    ws.send(JSON.stringify(obj))
}

window.open_modal = (id) => {
    is_modal_opend = true;
    currentModal = id;
    try {
        message_icon.style.display = 'none'
    } catch (e) {
        console.log(e)
    }
    Array.from(message_icon_chat).forEach(mov => {
        if (id == mov.id) {
            mov.style.display = 'none'
            //will be status set to 0 function
            set_status(id, 0);
        }
    })

    content.innerHTML += modal(id);
    let comments = getComments(id);
    comments.then(data => data.forEach(comment => {
        if (comment.username === sessionStorage.getItem("name")) {
            com_part.innerHTML += user_comment1(comment)
        } else {
            com_part.innerHTML += user_comment(comment)
        }
    }))
}

window.closing_filter = (child) => {
    is_modal_opend = false;
    currentModal = 0;
    child.parentElement.remove();
}


main.addEventListener("mousedown", function (e) {
    if (is_modal_opend) {
        const modal = document.getElementsByClassName("modal_box");
        if (modal.length != 0) {
            if (!modal[0].contains(e.target)) {
                for (let i = 0; i < modal.length; ++i) {
                    modal[i].remove()
                }
            }
        }
    }
}) 
