package com.example.moviesapp.controller

import androidx.fragment.app.Fragment
import com.squareup.picasso.RequestCreator

interface Communicator {
    fun passGenre(fragment: Fragment, genreId: Int, genreName: String)
    fun passMovie(fragment: Fragment, title: String, description: String, imageURL: String, genre: String, rating: Double)
}