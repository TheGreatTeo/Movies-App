package com.example.moviesapp.fragments.MainActivityFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.TMDBJSON
import com.example.moviesapp.R
import com.example.moviesapp.controller.ViewPagerAdapter.Adapter
import com.example.moviesapp.data.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class HomeFragment : Fragment(R.layout.fragment_home) {
    var movieList = ArrayList<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf<com.example.moviesapp.API.tmdbAPI.Result>())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var viewPager: ViewPager = view.findViewById(R.id.viewPager)
        movieList = ArrayList<MovieItem>()
        val adapter: Adapter = Adapter(movieList,this@HomeFragment.requireContext())
        progressBar.visibility = View.VISIBLE
        lifecycleScope.launch() {
            getMovies()
            viewPager.adapter = adapter
            viewPager.setPadding(100,0,100,0)
            viewPager.currentItem = movieList.size/2
            delay(200L)
            progressBar.visibility = View.GONE
        }

        return view
    }

    suspend fun getMovies() {
        return withContext(Dispatchers.IO) {
            val movies = TMDBInterface.create().getPopular("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if (movies.isSuccessful) {
                tmdbJSON = movies.body()!!
                Log.d("Popular", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    val movieItem = MovieItem(i.title,i.overview, Picasso.get().load("https://image.tmdb.org/t/p/w500" + i.poster_path),"",i.vote_average,i.poster_path)
                    movieList.add(movieItem)
                }
            }
        }
    }
}