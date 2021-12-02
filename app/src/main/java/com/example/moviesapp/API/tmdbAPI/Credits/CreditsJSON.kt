package com.example.moviesapp.API.tmdbAPI.Credits

data class CreditsJSON(
    val cast: List<Cast>,
    val crew: List<Crew>,
    val id: Int
)