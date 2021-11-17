package com.example.moviesapp.fragments.MainActivityFragments

import com.example.moviesapp.controller.RecyclerViewAdapters.GenreAdapter
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
import com.example.moviesapp.API.tmdbAPI.Genre
import com.example.moviesapp.API.tmdbAPI.GenreJSON
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.data.GenreItem
import com.example.moviesapp.fragments.MovieFragments.MovieFragment
import kotlinx.coroutines.*
import retrofit2.awaitResponse

class DashboardFragment : Fragment(R.layout.fragment_dashboard), GenreAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    private val movieFragment = MovieFragment()
    var genres = GenreJSON(listOf<Genre>())
    var genreList = ArrayList<GenreItem>()
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
        lifecycleScope.launch() {
            getGenres()
            delay(500L)
            recyclerView.adapter = GenreAdapter(genreList,this@DashboardFragment)
            recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            recyclerView.setHasFixedSize(true)
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.VISIBLE
        }
        return view
    }
    override fun onItemClick(position: Int) {
        communicator.passData(movieFragment,position)
    }

    suspend fun getGenres() {
        return withContext(Dispatchers.IO) {
            val genre =
                    TMDBInterface.create().getGenres("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if (genre.isSuccessful) {
                genres = genre.body()!!
                Log.d("Genres", genres.toString())
                for (i in 0 until genres.genres.size) {
                    val genre = genres.genres.get(i).name
                    val genreItem = GenreItem(genre, R.drawable.action_crop)
                    genreList.add(genreItem)
                }
            }
        }
    }
}