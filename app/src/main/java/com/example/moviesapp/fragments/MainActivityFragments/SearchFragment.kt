package com.example.moviesapp.fragments.MainActivityFragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.SearchView
import androidx.core.app.ActivityCompat.recreate
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.tmdbAPI.Result
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.TMDBJSON
import com.example.moviesapp.Activities.CoroutinesHomework
import com.example.moviesapp.Activities.LogInActivity
import com.example.moviesapp.R
import com.example.moviesapp.controller.ActivityOpener
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.MovieItem
import com.example.moviesapp.fragments.MovieFragments.MovieDetailsFragment
import com.example.moviesapp.fragments.MovieFragments.MovieFragment
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import java.util.*

class SearchFragment : Fragment(R.layout.fragment_search), MovieAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    var genreId: Int = -1
    var sharedPrefs = SharedPrefsHandler()
    val movieDetailsFragment = MovieDetailsFragment()
    var movieList = ArrayList<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        val searchBar: SearchView = view.findViewById(R.id.searchBar)
        var recyclerView: RecyclerView = view.findViewById(R.id.movieList)


        var progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        communicator = activity as Communicator

        searchBar.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchBar.clearFocus()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                movieList = ArrayList<MovieItem>()

                lifecycleScope.launch(){
                    if(movieList.isEmpty())
                        Log.d("Empty","Empty")
                    recyclerView.visibility = View.GONE
                    progressBar.visibility = View.VISIBLE
                    searchMovie(newText!!)
                    recyclerView.adapter = MovieAdapter(movieList,this@SearchFragment)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                    recyclerView.setHasFixedSize(true)
                    delay(250L)
                    recyclerView.visibility = View.VISIBLE
                    progressBar.visibility = View.GONE
                }
                return false
            }
        })

        return view
    }

    override fun onItemClick(position: Int) {
        val genresId = sharedPrefs.getGenresId(requireActivity())
        val genresName = sharedPrefs.getGenresName(requireActivity())
        communicator.passGenre(movieDetailsFragment, genresId.get(position).toInt(),genresName.get(position))
    }

    suspend fun searchMovie(query: String) {
        return withContext(Dispatchers.IO) {
            val movies = TMDBInterface.create().searchMovie("9df4f48f58d1cb4702a2b4d936029e0d",query).awaitResponse()
            if (movies.isSuccessful) {
                tmdbJSON = movies.body()!!
                Log.d("SearchedMovies", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    val movieItem = MovieItem(i.id,i.title,i.overview,i.poster_path,i.vote_average,i.poster_path)
                    movieList.add(movieItem)
                }
            }
        }
    }

}