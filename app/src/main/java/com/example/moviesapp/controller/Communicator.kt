package com.example.moviesapp.controller

import androidx.fragment.app.Fragment

interface Communicator {
    fun passData(fragment: Fragment, data: Int)
    fun passMovie(fragment: Fragment, genre: Int, movie: Int)
}