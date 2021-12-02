package com.example.moviesapp.fragments.MovieFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.RoomDB.*
import com.example.moviesapp.controller.Callback
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.GoBack
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieListAdapter
import com.example.moviesapp.data.Movie.MovieItem

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnItemClickListener, MovieListAdapter.OnItemClickListener,
    GoBack {

    var callbackFragment:Callback? = null
    private lateinit var communicator: Communicator
    var genreId: Int = -1
    var genreName: String = ""
    var movieDetailsFragment = MovieDetailsFragment()
    var movieList = listOf<MovieItem>()
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)
        communicator = activity as Communicator
        movieList = ArrayList()
        genreId = arguments?.getInt("genreId")!!
        genreName = arguments?.getString("genreName")!!


        var recyclerView: RecyclerView = view.findViewById(R.id.movieList)
        var adapter = MovieListAdapter(this)
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
        movieDetailsFragment.getGoBack(this)
        communicator.passMovie(movieDetailsFragment,movieItem.id)
    }

    override fun goBack() {
        callbackFragment?.changeFragment()
        Log.d("Clicked","Click!2")
    }

    fun getCallback(callback: Callback){
        this.callbackFragment = callback
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