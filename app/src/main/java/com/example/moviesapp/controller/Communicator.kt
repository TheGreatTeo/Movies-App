package com.example.moviesapp.controller

import androidx.fragment.app.Fragment
import com.squareup.picasso.RequestCreator

interface Communicator {
    fun passGenre(fragment: Fragment, genreId: Int, genreName: String)
    fun passMovie(fragment: Fragment, id: Int)
    fun searchView(fragment: Fragment)
}