export const movieFilterPart = () => {
    return `
    <div class="filter">
    <div class="filter_part">
            <select id="genre">
                <option value="">Genre</option>
                <option value="Action">Action</option>
                <option value="Adventure">Adventure</option>
                <option value="Comedy">Comedy</option>
                <option value="Crime">Crime</option>
                <option value="Fantasy">Fantasy</option>
                <option value="Historical">Historical</option>
                <option value="Romance">Romance</option>
                <option value="Thriller">Thriller</option>
                <option value="Drama">Drama</option>
                <option value="Biography">Biography</option>
                <option value="Musical">Musical</option>
            </select>
        </div>
        <div class="filter_part">
            <select id="rating">
                <option value="">Rating</option>
                <option value="hf">Highest first</option>
                <option value="lf">Lowest first</option>
            </select>
        </div>
        <div class="filter_part">
            <select id="year">
                <option value="">Year</option>
                <option value="bef">Befor</option>
                <option value="aft">After</option>
            </select>
            <input id="year_val" type="number" placeholder="Enter the year">
        </div>
        <div class="filter_part">
            <select id="celebrity">
                <option value="">Celebrity</option>
                <option value="director">Director</option>
                <option value="actor">Actor</option>
            </select>
            <input id="celebrity_value" type="text" placeholder="Enter Name">
        </div>
        <div class="filter_part"> 
            <button  id="find" onclick="loadFilms()">FIND</button> 
            <button  id="reset" onclick="clearFileds()">RESET</button>
        </div>   
        
        
        </div>
        <div id="movies_with_rate">
    
        </div> 
    `
}

export const mainPageCont = () => {
    return `
    <div class="carousel" id="carousel" data-flickity='{ "wrapAround": true, "autoPlay": 3000,"imagesLoaded": true }'>
    
        <div class="carousel-cell">
          <img class="carousel-cell-image" src="https://images4.alphacoders.com/674/674634.jpg" />
         </div>
        <div class="carousel-cell">
          <img class="carousel-cell-image" src="https://images.squarespace-cdn.com/content/v1/56ac07cee3214002760328ef/1612726758644-NBPO8BF5FGVSL02NZTKV/pulp+fiction+dance.png?format=1000w" />
        </div>
         <div class="carousel-cell">
          <img class="carousel-cell-image" src="https://cdn.flickeringmyth.com/wp-content/uploads/2019/12/The-Gentlemen.png" />
        </div>
        <div class="carousel-cell">
          <img class="carousel-cell-image" src="https://www.pngall.com/wp-content/uploads/5/Joker-Movie-PNG-Free-Image.png" />
        </div>
        <div class="carousel-cell">
          <img class="carousel-cell-image" src="https://images.fanart.tv/fanart/scent-of-a-woman-51427b99bcf3f.png" />
        </div>
      </div>

      <div class="mainPageMovies">

      <div class="mainMovPart">
        <h3>Most popular among users</h3>
        <div class="films" id="popularMovies">
  
        </div>
      </div>
      
      <div class="mainMovPart">
        <h3>Highest rating in MovieCommunity</h3>
        <div class="films" id="highratedMovies">
        </div>
      </div>
    </div>
      
     
    `

}


export const AC_DIR_FilterPart = () => {
    return `
    <div class="filter">
        <div class="filter_part">
            <input id="celebrity_value" type="text" placeholder="Enter Name">
        </div>
        <div class="filter_part">
            <select id="year">
                <option value="">Year</option>
                <option value="bef">Befor</option>
                <option value="aft">After</option>
            </select>
            <input id="year_val" type="number" placeholder="Enter the year">
        </div>
        <div class="filter_part">
          <select id="gender">
              <option value="">Gender</option>
              <option value="m">Male</option>
              <option value="f">Female</option>
          </select>
      </div>
      <div class="filter_part">
        <select id="country">
            <option value="">Country</option>
            <option value="USA">USA</option>
            <option value="United Kingdom">United Kingdom</option>
            <option value="Ukraine">Ukraine</option>
            <option value="Israel">Israel</option>
            <option value="Australia">Australia</option>
            <option value="Canada">Canada</option>
            <option value="New Zealand">New Zealand</option>
            <option value="Lebanon">Lebanon</option>
            <option value="South Africa">South Africa</option>
            <option value="Italy">Italy</option>
        </select>
        </div>
        
    `
}

export const find_button_actor = () => {
    return `
    <div class="filter_part"> 
        <button  id="find" onclick="loadActor()">FIND</button> 
        <button  id="reset" onclick="clearFiledsCel()">RESET</button>
    </div>   
    </div>
    <div id="found_films">
        
    </div> 
    `
}

export const addInfoSellActor = (obj) => {
    return `
        <div class="infoSell">
            <img class="img" src="${obj.url}" alt="">
            <a onclick="goToActorInfo(${obj.id})"></i> ${obj.name}</a>
        </div>`
}

export const addInfoSellMovie = (obj) => {
    return `
        <div class="infoSell">
            <img class="img" src="${obj.url}" alt="">
            <a  onclick="goToMovie(${obj.id}, 0)"></i> ${obj.name}</a>
        </div>`
}

export const addInfoSellMovieForList = (obj) => {
    return `
        <div class="infoSell">
        <svg class="close" onclick="remFromWatchlist(${obj.id}, this)" stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 512 512" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg"><path d="M289.94 256l95-95A24 24 0 00351 127l-95 95-95-95a24 24 0 00-34 34l95 95-95 95a24 24 0 1034 34l95-95 95 95a24 24 0 0034-34z"></path></svg>      
            <img class="img" src="${obj.url}" alt="">
            <a  onclick="goToMovie(${obj.id}, 0)"></i> ${obj.name}</a>
        </div>`
}


export const addMovieMainPage = (obj) => { // missing rating and review
    let genres = obj.genres;
    let genresCont = "<h4>Genres</h4>";
    for (let i = 0; i < genres.length; ++i) {
        genresCont += `<span> <i>${genres[i]} </i></span>`
    }

    let movie = `<div class="celeb_page">
    <div class="md-6 pic-div">
      <div>
        <img class="img1" src="${obj.url}" alt="">
    </div>
    <div class="info" id="movieInfo">
        <h3>${obj.name}</h3>
        <p>Director: <a onclick="goToDirectorInfo(${obj.dir_id})"> <span style="color:#0e34dd;">${obj.dir_name}</span></a></p>
        <p>Filmed: ${obj.mov_year}</p>
        <div id="genres">`

    movie += genresCont;

    movie += `
    </div>
        <div>
          <h4>Description</h4>
          ${obj.mov_desc}
        </div>
        <div id="ratingDiv">
    `

    movie += `
        Rating: ${obj.rating}
        </div>
    `


    movie += `
    <div class="message_info">
    <svg class="new_message" id="message_icon"stroke="currentColor" fill="none" stroke-width="2" viewBox="0 0 22 22" stroke-linecap="round" stroke-linejoin="round" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg"><desc></desc><path stroke="none" d="M0 0h24v24H0z" fill="none"></path><path d="M3 20l1.3 -3.9a9 8 0 1 1 3.4 2.9l-4.7 1"></path><line x1="12" y1="12" x2="12" y2="12.01"></line><line x1="8" y1="12" x2="8" y2="12.01"></line><line x1="16" y1="12" x2="16" y2="12.01"></line></svg>
    <button  id="open_review" onclick="open_modal(${obj.id})">MOVIE ROOM</button> 
    </div>
         
    </div>
    </div>
    <div class="films md-6" id="films">
    </div>
    `
    return movie;
}

export const user_comment = (obj) => {
    return `
    <div class="user">
          <span class="user_name"><b><i>${obj.username}</i></b></span><br><br>
          <span class="rev">${obj.comment} </span>
      </div> 
    `
}

export const user_comment1 = (obj) => {
    return `
    <div class="user1">
          <span class="user_name"><b><i>You</i></b></span><br><br>
          <span class="rev1">${obj.comment} </span>
      </div> 
    `
}

export const modal = (id) => {

    let modal_box = `
    <div class="modal_box">
        <svg class="close" onclick="closing_filter(this)" stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 512 512" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg"><path d="M289.94 256l95-95A24 24 0 00351 127l-95 95-95-95a24 24 0 00-34 34l95 95-95 95a24 24 0 1034 34l95-95 95 95a24 24 0 0034-34z"></path></svg>      
        <div class="wtchpanel">Movie Room</div>
        <div class="comment_part">
          <div class="review" id="com_part">
            
        </div>
        <div class="user_comment">
          <div>
            <input type="text" id="comment">
          </div>
          <div>
    `

    if (sessionStorage.getItem("name") != null) {
        modal_box += `
        <button class="add_comm" onclick="addUserComent(${id})">Add</button>
          </div>
        </div>
        </div>
      </div>
        `
    } else {
        modal_box += `
        <button class="add_comm" onclick="loadLoginPage()">SIGN IN</button>
          </div>
        </div>
        </div>
      </div>
        `
    }

    return modal_box;
}

export const addInfoSellDirector = (obj) => {
    return `
        <div class="infoSell">
            <img class="img" src="${obj.url}" alt="">
            <a  onclick="goToDirectorInfo(${obj.id})"></i> ${obj.name}</a>
        </div>`
}

export const addInfoSellMovieRate = (obj) => {
    return `
    <div class="movieInfo">
      <img class="imgM" src="${obj.url}" alt="">
      <div class="infoSellMovie">
        
        <a class="nameM"  onclick="goToMovie(${obj.id}, 0)"></i> ${obj.name}</a>
        <span class="rateM">Rating: ${obj.rating} </span>
    </div>
    </div>

        `
}

export const find_button_dir = () => {
    return `
    <div class="filter_part"> 
        <button  id="find" onclick="loadDir()">FIND</button>
        <button  id="reset" onclick="clearFiledsCel()">RESET</button> 
    </div>   
    </div>
    <div id="found_films">

    </div> 
    `
}

export const found_fav = () => {
    return `<div id="found_fav_films">

    </div> `
}

export const addCelebMainPage = (obj) => {
    return `
    <div class="celeb_page">
        <div>
            <img class="img1" src="${obj.url}" alt="">
        </div>
        <div class="info">
            <h3>${obj.name}</h3>
            <p>From ${obj.country}</p>
            <p>Born in ${obj.date}</p>
        </div>
        <div class="films" id="films">
            
        </div>
    </div>
    `
}

export const login_page = () => {
    return `
    <div class="lgin">
            <div class="user_p" id="register">
              <h3>Registration</h3>
              <input class="l_input" onkeydown="whiteBorder(this)" id="reg_username" type="text" placeholder="Username">
              <input class="l_input" onkeydown="whiteBorder(this)" id="reg_passwd" type="password" placeholder="Password">
              <button onclick="register_user()" id="reg_user">Sign Up</button>
            </div>
            <div class="user_p" id="reg_login">
              <h3>Already have account</h3>
              <input class="l_input" onkeydown="whiteBorder(this)" id="username" type="text" placeholder="Username">  
              <input class="l_input" onkeydown="whiteBorder(this)" id="passwd" type="password" placeholder="Password">
              <button onclick="login_user()" id="log_user">Sign In</button>
            </div>
          </div>
    `
}


export const movie_panel = (movie) => {
    return `
    <div id="chatmovies">
       <div onclick="open_modal(${movie.id})" class="chatname">
       <svg class="message_icon_chat" id="${movie.id}" stroke="currentColor" fill="currentColor" stroke-width="0" viewBox="0 0 16 16" height="1em" width="1em" xmlns="http://www.w3.org/2000/svg"><path d="M0 2a2 2 0 0 1 2-2h12a2 2 0 0 1 2 2v8a2 2 0 0 1-2 2h-2.5a1 1 0 0 0-.8.4l-1.9 2.533a1 1 0 0 1-1.6 0L5.3 12.4a1 1 0 0 0-.8-.4H2a2 2 0 0 1-2-2V2zm3.5 1a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9zm0 2.5a.5.5 0 0 0 0 1h9a.5.5 0 0 0 0-1h-9zm0 2.5a.5.5 0 0 0 0 1h5a.5.5 0 0 0 0-1h-5z" style="
        "></path></svg>
      ${movie.name}</div>
    </div>
    `
}
