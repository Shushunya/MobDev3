package ua.kpi.comsys.iv7124.mymovies.ui.movies

class Movie (val Title: String,
             val Year: String,
             var Rated: String,
             var Released: String,
             var Runtime: String,
             var Genre: String,
             var Director: String,
             var Writer: String,
             var Actors: String,
             var Plot: String,
             var Language: String,
             var Country: String,
             var Awards: String,
             var Poster: String,
             var imdbRating: String,
             var imdbVotes: String,
             var imdbID: String,
             var Type: String,
             var Production: String) {
    constructor(Title: String, Year: String, imdbID: String, Type: String, Poster: String) : this(Title, Year, "", "", "",
        "", "", "","","","","","",
        Poster,"","",imdbID, Type,"")

    constructor() : this("", "", "", "", "",
        "", "", "","","","","","",
        "","","","","","")
}

class MovieSearchResult {
    var Search: List<Movie> = listOf()
}
