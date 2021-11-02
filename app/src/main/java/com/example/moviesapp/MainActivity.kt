package com.example.moviesapp

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.controller.MovieAdapter
import com.example.moviesapp.data.MovieItem
import org.json.JSONArray
import java.io.IOException
import java.net.URI

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var recyclerView: RecyclerView = findViewById(R.id.moviesList)
        val jsonArray = readJSON()
        val movieList = ArrayList<MovieItem>()

        for(i in 0 until jsonArray.length()){
            val url = jsonArray.getJSONObject(i).getString("imageURL")
            val title = jsonArray.getJSONObject(i).getString("title")
            val rating = jsonArray.getJSONObject(i).getDouble("rating")
            val movieItem = MovieItem(R.drawable.poster,title,rating)
            movieList.add(movieItem)
        }
        recyclerView.adapter = MovieAdapter(movieList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
    }

    private fun readJSON() : JSONArray{
        var json: String? = null
        var jsonArray: JSONArray = JSONArray()
        try{
            val inputStream = resources.openRawResource(resources.getIdentifier("movies","raw",packageName))
            json = inputStream.bufferedReader().use { it.readText() }

            jsonArray = JSONArray(json)
        }catch (e : IOException){
            Log.d("IOException",e.printStackTrace().toString())
        }
        return jsonArray
    }
}