package com.example.moviesapp.data.Genre

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity
data class Genre(@PrimaryKey(autoGenerate = true)val gnrId: Int, val movieId: Int, val genreId : Int,val genreName: String) {

}