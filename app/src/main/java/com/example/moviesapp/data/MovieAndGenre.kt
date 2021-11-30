package com.example.moviesapp.data

import androidx.room.Embedded
import androidx.room.Relation
import com.example.moviesapp.data.Genre

data class MovieAndGenre(@Embedded val movieItem: MovieItem,@Relation(parentColumn = "id", entityColumn = "movieId") val genreIds: List<Genre>) {
}