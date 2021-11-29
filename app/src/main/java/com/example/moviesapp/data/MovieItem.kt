package com.example.moviesapp.data

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.moviesapp.RoomDB.ListConvertor
import com.squareup.picasso.RequestCreator

@Entity(tableName = "movie_table")
data class MovieItem(@PrimaryKey val id: Int, val title: String, val description: String, val imageResource: String?, val rating: Double, val imageURL: String?){

}
