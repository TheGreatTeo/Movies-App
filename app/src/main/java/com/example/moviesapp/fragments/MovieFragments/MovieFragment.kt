package com.example.moviesapp.fragments.MovieFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.tmdbAPI.Result
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.TMDBJSON
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.data.GenreItem
import com.example.moviesapp.data.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import retrofit2.awaitResponse
import java.io.IOException

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    var genreId: Int = -1
    var genreName: String = ""
    var movieDetailsFragment = MovieDetailsFragment()
    var movieList = ArrayList<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf<Result>())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        communicator = activity as Communicator
        movieList = ArrayList<MovieItem>()
        genreId = arguments?.getInt("genreId")!!
        genreName = arguments?.getString("genreName")!!
        Log.d("genreId",genreId.toString())
        var recyclerView: RecyclerView = view.findViewById(R.id.movieList)

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        lifecycleScope.launch(){
            getMovies()
            recyclerView.adapter = MovieAdapter(movieList,this@MovieFragment)
            recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            recyclerView.setHasFixedSize(true)
            delay(250L)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }

        return view
    }

    override fun onItemClick(position: Int) {
        val movieItem = movieList.get(position)
        //communicator.passMovie(movieDetailsFragment,movieItem.title,movieItem.description,movieItem.imageURL,movieItem.genre,movieItem.rating)
    }

    suspend fun getMovies() {
        return withContext(Dispatchers.IO) {
            val movies = TMDBInterface.create().getTopRatedMovies("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if (movies.isSuccessful) {
                tmdbJSON = movies.body()!!
                Log.d("Genres", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    if(genreId in i.genre_ids) {
                        val movieItem = MovieItem(i.title,i.overview,Picasso.get().load("https://image.tmdb.org/t/p/w500" + i.poster_path),"",i.vote_average,i.poster_path)
                        movieList.add(movieItem)
                    }
                }
            }
        }
    }

}