package com.example.moviesapp.API.tmdbAPI

data class CreditsJSON(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)