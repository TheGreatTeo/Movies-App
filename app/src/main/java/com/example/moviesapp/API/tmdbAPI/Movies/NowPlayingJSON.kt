package com.example.moviesapp.API.tmdbAPI.Movies

data class NowPlayingJSON(
    val dates: Dates,
    val page: Int,
    val results: List<Result>
)