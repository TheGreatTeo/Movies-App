package com.example.moviesapp.data.Movie

open class MovieLibrary(genrePos: Int,moviePos:Int,userID: String) {
    var genrePos = 0
    var moviePos = 0
    var userID = ""

    init {
        this.genrePos = genrePos
        this.moviePos = moviePos
        this.userID = userID
    }
}