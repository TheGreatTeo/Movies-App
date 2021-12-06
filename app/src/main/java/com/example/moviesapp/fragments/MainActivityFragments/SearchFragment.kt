package com.example.moviesapp.fragments.MainActivityFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.tmdbAPI.Genres.GenreJSON
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.Movies.TMDBJSON
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.RoomDB.MovieViewModel
import com.example.moviesapp.RoomDB.MovieViewModelFactory
import com.example.moviesapp.RoomDB.MoviesApplication
import com.example.moviesapp.controller.CheckInternet
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.Genre.Genre
import com.example.moviesapp.data.Movie.MovieItem
import com.example.moviesapp.fragments.MovieFragments.MovieDetailsFragment
import kotlinx.coroutines.*
import retrofit2.awaitResponse
import java.util.*

class SearchFragment : Fragment(R.layout.fragment_search), MovieAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    var genreId: Int = -1
    var sharedPrefs = SharedPrefsHandler()
    val checkInternet= CheckInternet()
    val movieDetailsFragment = MovieDetailsFragment()
    var movieList = arrayListOf<MovieItem>()
    var movieListSaved = arrayListOf<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf())
    var genreJSON = GenreJSON(listOf())
    var genresList = hashMapOf<Int,ArrayList<com.example.moviesapp.API.tmdbAPI.Genres.Genre>>()
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchBar: SearchView = view.findViewById(R.id.searchBar)
        var recyclerView: RecyclerView = view.findViewById(R.id.movieList)
        var missingData: ImageView = view.findViewById(R.id.missingData)

        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        communicator = activity as Communicator

        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieList = arrayListOf<MovieItem>()

                if (checkInternet.isOnline(requireContext())) {
                    lifecycleScope.launch() {
                        recyclerView.visibility = View.GONE
                        missingData.visibility = View.GONE
                        progressBar.visibility = View.VISIBLE
                        searchMovie(newText!!)
                        recyclerView.adapter = MovieAdapter(movieList, this@SearchFragment)
                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext().applicationContext)
                        recyclerView.setHasFixedSize(true)
                        if (movieListSaved.isEmpty()) {
                            missingData.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        } else {
                            recyclerView.visibility = View.VISIBLE
                            progressBar.visibility = View.GONE
                        }
                    }

                }else{
                    missingData.visibility = View.VISIBLE
                    Toast.makeText(requireContext(),"No internet connection!", Toast.LENGTH_SHORT).show()
                }
                return false
            }
        })

        return view
    }

    override fun onItemClick(position: Int) {
        val movieItem = movieListSaved.get(position)
        lifecycleScope.launch() {
            checkMovie(movieItem)
            Log.d("movieItem", movieItem.id.toString())
            communicator.passMovie(movieDetailsFragment, movieItem.id)
        }
    }

    suspend fun searchMovie(query: String) {
        return withContext(Dispatchers.IO) {
            val movies = TMDBInterface.create().searchMovie(BuildConfig.API_KEY,query).awaitResponse()
            val genres = TMDBInterface.create().getGenres(BuildConfig.API_KEY).awaitResponse()
            if (movies.isSuccessful) {
                tmdbJSON = movies.body()!!
                Log.d("SearchedMovies", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    if(i.vote_average != 0.0) {
                        val movieItem = MovieItem(
                            i.id,
                            i.title,
                            i.overview,
                            i.poster_path,
                            i.vote_average
                        )
                        Log.d("GenreIds",i.genre_ids.toString())
                        val genresArray = ArrayList<com.example.moviesapp.API.tmdbAPI.Genres.Genre>()
                        for(j in i.genre_ids){
                            if(genres.isSuccessful){
                                genreJSON = genres.body()!!
                                for(genre in genreJSON.genres){
                                    if(j == genre.id)
                                        genresArray.add(genre)
                                        genresList.put(i.id,genresArray)
                                }
                            }
                        }
                        movieList.add(movieItem)
                    }
                }
                movieListSaved = movieList
            }
        }
    }

    suspend fun checkMovie(movieItem: MovieItem){
        lifecycleScope.async(Dispatchers.IO) {
            if (!viewModel.movieExists(movieItem.id)) {
                viewModel.insertMovie(movieItem)
                for (i in genresList) {
                    if (i.key == movieItem.id) {
                        for (gnr in i.value) {
                            val genre = Genre(0, movieItem.id, gnr.id, gnr.name)
                            viewModel.insertGenre(genre)
                        }
                    }
                }
            }
        }.await()
    }

}