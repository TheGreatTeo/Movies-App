package com.example.moviesapp.data

import android.net.Uri
import com.squareup.picasso.RequestCreator

data class MovieItem(val title: String, val description: String, val imageResource: RequestCreator, val genre: String, val rating: Double,val imageURL: String?){

}
