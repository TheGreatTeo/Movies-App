package com.example.moviesapp.data.Movie

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moviesapp.data.Genre.Genre

data class MovieAndGenre(@Embedded val movieItem: MovieItem, @Relation(parentColumn = "id", entityColumn = "movieId") val genreIds: List<Genre>) {
}