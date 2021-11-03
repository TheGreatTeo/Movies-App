package com.example.moviesapp.fragments

import GenreAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.data.GenreItem
import org.json.JSONArray
import java.io.IOException

class DashboardFragment : Fragment(R.layout.fragment_dashboard) {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.genreList)
        val jsonArray = readJSON()
        val genreList = ArrayList<GenreItem>()

        for(i in 0 until jsonArray.length()){
            val genre = jsonArray.getJSONObject(i).getString("genre")
            val genreItem = GenreItem(genre,R.drawable.action_crop)
            genreList.add(genreItem)
        }

        recyclerView.adapter = GenreAdapter(genreList)
        recyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
        recyclerView.setHasFixedSize(true)

        return view
    }

    private fun readJSON() : JSONArray {
        var json: String? = null
        var jsonArray: JSONArray = JSONArray()
        try{
            val inputStream = resources.openRawResource(resources.getIdentifier("genres","raw",context!!.applicationContext.packageName))
            json = inputStream.bufferedReader().use { it.readText() }

            jsonArray = JSONArray(json)
        }catch (e : IOException){
            Log.d("IOException",e.printStackTrace().toString())
        }
        return jsonArray
    }
}