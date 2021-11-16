package com.example.moviesapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.IMDBInterface
import com.example.moviesapp.API.TMDBInterface
import com.example.moviesapp.R
import com.example.moviesapp.data.GenreItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import retrofit2.awaitResponse
import java.io.IOException

class HomeFragment : Fragment(R.layout.fragment_home) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val textView: TextView = view.findViewById(R.id.textView)
        progressBar.visibility = View.VISIBLE
        textView.visibility = View.GONE
        lifecycleScope.launch(Dispatchers.IO) {
            delay(500L)
            val tmdbInterface = TMDBInterface.create().getTopRatedMovies("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if(tmdbInterface.isSuccessful){
                val data = tmdbInterface.body()
                Log.d("Raspuns",data.toString())
            }

            val title = IMDBInterface.create().searchTitle("k_m2duxbz7","Harry Potter").awaitResponse()
            if(title.isSuccessful){
                val data = title.body()
                Log.d("Title",data.toString())
            }
            withContext(Dispatchers.Main){
                progressBar.visibility = View.GONE
                textView.visibility = View.VISIBLE
            }
        }

        return view
    }
}