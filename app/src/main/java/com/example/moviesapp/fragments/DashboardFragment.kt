package com.example.moviesapp.fragments

import GenreAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.Genre
import com.example.moviesapp.API.GenreJSON
import com.example.moviesapp.API.TMDBInterface
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.data.GenreItem
import kotlinx.coroutines.*
import org.json.JSONArray
import retrofit2.awaitResponse
import java.io.IOException

class DashboardFragment : Fragment(R.layout.fragment_dashboard),GenreAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    private val movieFragment = MovieFragment()
    var recyclerView: RecyclerView? = null
    var genres = GenreJSON(listOf<Genre>())
    var genreList = ArrayList<GenreItem>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        recyclerView = view.findViewById(R.id.genreList)

        communicator = activity as Communicator
        progressBar.visibility = View.VISIBLE
        recyclerView!!.visibility = View.GONE
        lifecycleScope.launch() {
            getGenres()
            delay(500L)
            recyclerView!!.adapter = GenreAdapter(genreList,this@DashboardFragment)
            recyclerView!!.layoutManager = LinearLayoutManager(requireContext().applicationContext)
            recyclerView!!.setHasFixedSize(true)
            progressBar.visibility = View.GONE
            recyclerView!!.visibility = View.VISIBLE
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