package com.example.moviesapp.API

data class TitleJSON(
    val expression: String,
    val results: List<ResultTitle>,
    val searchType: String
)