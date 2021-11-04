package com.example.moviesapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import org.json.JSONArray
import java.io.IOException

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    var genrePosition = -1
    var moviePosition = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val title: TextView = view.findViewById(R.id.title)
        val rating: TextView = view.findViewById(R.id.rating)
        val description: TextView = view.findViewById(R.id.description)
        val image: ImageView = view.findViewById(R.id.image)
        val addToWatchList: Button = view.findViewById(R.id.addToWatchList)

        genrePosition = arguments?.getInt("genrePosition")!!
        moviePosition = arguments?.getInt("moviePosition")!!

        val movie = readJSON().getJSONArray(genrePosition).getJSONObject(moviePosition)

        title.text = movie.getString("title")
        rating.text = "⭐"+movie.getDouble("rating")
        description.text = movie.getString("description")
        image.setImageResource(R.drawable.poster)

        return view
    }

    private fun readJSON() : JSONArray {
        var json: String? = null
        var jsonArray: JSONArray = JSONArray()
        try{
            val inputStream = resources.openRawResource(resources.getIdentifier("movies","raw",context!!.applicationContext.packageName))
            json = inputStream.bufferedReader().use { it.readText() }

            jsonArray = JSONArray(json)
        }catch (e : IOException){
            Log.d("IOException",e.printStackTrace().toString())
        }
        return jsonArray
    }
}