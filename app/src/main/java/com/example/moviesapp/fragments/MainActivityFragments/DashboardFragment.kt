package com.example.moviesapp.fragments.MainActivityFragments

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.moviesapp.controller.RecyclerViewAdapters.GenreAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.tmdbAPI.Genre
import com.example.moviesapp.API.tmdbAPI.GenreJSON
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.R
import com.example.moviesapp.controller.CheckInternet
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.GenreItem
import com.example.moviesapp.fragments.MovieFragments.MovieFragment
import kotlinx.coroutines.*
import retrofit2.awaitResponse

class DashboardFragment : Fragment(R.layout.fragment_dashboard), GenreAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    val sharedPrefs = SharedPrefsHandler()
    private val movieFragment = MovieFragment()
    var genres = GenreJSON(listOf<Genre>())
    var genreList = ArrayList<GenreItem>()
    val checkInternet= CheckInternet()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var recyclerView: RecyclerView = view.findViewById(R.id.genreList)
        communicator = activity as Communicator

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE

        genreList = ArrayList()
        var genreSet = sharedPrefs.getGenresName(requireActivity())
        if(checkInternet.isOnline(requireContext())) {
            if (genreSet.size == 1) {
                lifecycleScope.launch() {
                    Log.d("GETGenre", "GET")
                    getGenres()
                    delay(500L)
                    recyclerView.adapter = GenreAdapter(genreList, this@DashboardFragment)
                    recyclerView.layoutManager =
                        LinearLayoutManager(requireContext().applicationContext)
                    recyclerView.setHasFixedSize(true)
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            } else {
                lifecycleScope.launch() {
                    Log.d("GETGenre", "NOGET")
                    for (i in genreSet) {
                        val genreItem = GenreItem(i, R.drawable.action_crop)
                        genreList.add(genreItem)
                        recyclerView.adapter = GenreAdapter(genreList, this@DashboardFragment)
                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext().applicationContext)
                        recyclerView.setHasFixedSize(true)
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }else{
            Toast.makeText(requireContext(),"No internet connection!",Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        return view
    }
    override fun onItemClick(position: Int) {
        val genresId = sharedPrefs.getGenresId(requireActivity())
        val genresName = sharedPrefs.getGenresName(requireActivity())
        communicator.passGenre(movieFragment, genresId.get(position).toInt(),genresName.get(position))
        }

    suspend fun getGenres() {
        return withContext(Dispatchers.IO) {
            val genre =
                    TMDBInterface.create().getGenres("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            var genresId = mutableListOf<String>()
            var genresName = mutableListOf<String>()
            if (genre.isSuccessful) {
                genres = genre.body()!!
                Log.d("Genres", genres.toString())
                for (i in 0 until genres.genres.size) {
                    val genre = genres.genres.get(i).name
                    Log.d("genreList",genre + " " + genres.genres[i].id)
                    val genreItem = GenreItem(genre, R.drawable.action_crop)
                    genreList.add(genreItem)
                    genresId.add(genres.genres[i].id.toString())
                    genresName.add(genres.genres[i].name)
                }
                Log.d("genres",genresId.toString() + "\n" + genresName.toString())
                sharedPrefs.setGenres(requireActivity(),genresId,genresName)
            }
        }
    }
}