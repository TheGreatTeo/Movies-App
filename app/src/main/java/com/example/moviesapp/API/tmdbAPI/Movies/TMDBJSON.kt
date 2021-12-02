package com.example.moviesapp.API.tmdbAPI.Movies

data class TMDBJSON(
    val page: Int,
    val results: List<Result>
)