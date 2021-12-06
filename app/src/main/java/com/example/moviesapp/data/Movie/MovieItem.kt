package com.example.moviesapp.data.Movie

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieItem(@PrimaryKey val id: Int, val title: String, val description: String, val imageResource: String?, val rating: Double,val added: Boolean = false){

}
