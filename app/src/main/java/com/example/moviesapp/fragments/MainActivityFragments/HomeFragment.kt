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
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

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
        lifecycleScope.launch() {
            test()
            delay(200L)
            progressBar.visibility = View.GONE
            textView.visibility = View.VISIBLE
        }

        return view
    }

    suspend fun test(){
        return withContext(Dispatchers.IO){
            val tmdbInterface = TMDBInterface.create().getTopRatedMovies("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if(tmdbInterface.isSuccessful){
                val data = tmdbInterface.body()
                Log.d("Raspuns",data.toString())
            }
        }
    }
}