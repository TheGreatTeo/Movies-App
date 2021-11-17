package com.example.moviesapp.API.tmdbAPI

data class TMDBJSON(
    val page: Int,
    val results: List<Result>
)