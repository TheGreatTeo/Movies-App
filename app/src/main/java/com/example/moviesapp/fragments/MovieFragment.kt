package com.example.moviesapp.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.MovieAdapter
import com.example.moviesapp.data.GenreItem
import com.example.moviesapp.data.MovieItem
import org.json.JSONArray
import java.io.IOException

class MovieFragment : Fragment(R.layout.fragment_movie), MovieAdapter.OnItemClickListener {

    private lateinit var communicator: Communicator
    var position: Int = -1
    var movieDetailsFragment = MovieDetailsFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        position = arguments?.getInt("genrePosition")!!
        Log.d("genrePosition",position.toString())

        var recyclerView: RecyclerView = view.findViewById(R.id.movieList)
        val jsonArray = readJSON().getJSONArray(position)
        var movieList = ArrayList<MovieItem>()

        communicator = activity as Communicator

        for(i in 0 until jsonArray.length()){
            val title = jsonArray.getJSONObject(i).getString("title")
            val rating = jsonArray.getJSONObject(i).getDouble("rating")
            val movieItem = MovieItem(R.drawable.poster,title,rating)
            movieList.add(movieItem)
        }

        recyclerView.adapter = MovieAdapter(movieList,this)
        recyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
        recyclerView.setHasFixedSize(true)

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

    override fun onItemClick(position: Int) {
        communicator.passMovie(movieDetailsFragment,this.position,position)
    }

}