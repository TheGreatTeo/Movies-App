package com.example.moviesapp.fragments.MovieFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.tmdbAPI.Result
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.TMDBJSON
import com.example.moviesapp.R
import com.example.moviesapp.RoomDB.*
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieListAdapter
import com.example.moviesapp.data.GenreItem
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieItem
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import retrofit2.awaitResponse
import java.io.IOException

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnItemClickListener, MovieListAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    var genreId: Int = -1
    var genreName: String = ""
    var movieDetailsFragment = MovieDetailsFragment()
    var movieList = listOf<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf<Result>())
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }
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

        var recyclerView: RecyclerView = view.findViewById(R.id.movieList)
        var adapter = MovieListAdapter(this@MovieFragment)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getMovieByGenre(genreId).observe(viewLifecycleOwner){ movies ->
            adapter.setMovies(movies)
            movieList = movies
        }

        return view
    }

    override fun onItemClick(position: Int) {
        val movieItem = movieList.get(position)
        communicator.passMovie(movieDetailsFragment,movieItem.id)
    }

/*    suspend fun getMovies() {
        return withContext(Dispatchers.IO) {
            val movies = TMDBInterface.create().getTopRatedMovies("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if (movies.isSuccessful) {
                tmdbJSON = movies.body()!!
                Log.d("Genres", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    if(genreId in i.genre_ids) {
                        val movieItem = MovieItem(i.id,i.title,i.overview,i.poster_path,"",i.vote_average,i.poster_path)
                        viewModel.insert(movieItem)
                    }
                }
            }
        }
    }*/

}