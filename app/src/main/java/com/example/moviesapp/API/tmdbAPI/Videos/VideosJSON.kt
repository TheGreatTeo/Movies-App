package com.example.moviesapp.API.tmdbAPI.Videos

data class VideosJSON(
    val id: Int,
    val results: List<Result>
)