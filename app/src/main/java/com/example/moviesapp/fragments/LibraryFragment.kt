package com.example.moviesapp.fragments

import android.graphics.Movie
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.controller.MovieAdapter
import com.example.moviesapp.data.MovieItem
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.JsonObject
import org.json.JSONArray
import java.io.IOException

class LibraryFragment : Fragment(R.layout.fragment_library),MovieAdapter.OnItemClickListener {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        var recyclerView: RecyclerView = view.findViewById(R.id.movieLibraryList)
        var movieList = ArrayList<MovieItem>()
        val userID = FirebaseAuth.getInstance().currentUser?.uid

        FirebaseFirestore.getInstance().collection("movieLibrary").whereEqualTo("userID",userID).get().addOnSuccessListener(
            OnSuccessListener { documents ->
                for(document in documents){
                    Log.d("Docu",document.id)
                    val jsonObject = readJSON().getJSONArray(document.getLong("genrePos")!!.toInt()).getJSONObject(document.getLong("moviePos")!!.toInt())
                    val movieItem = MovieItem(R.drawable.poster,jsonObject.getString("title"),jsonObject.getDouble("rating"))
                    movieList.add(movieItem)

                    recyclerView.adapter = MovieAdapter(movieList,this)
                    recyclerView.layoutManager = LinearLayoutManager(context!!.applicationContext)
                    recyclerView.setHasFixedSize(true)
                }
            })


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
        TODO("Not yet implemented")
    }
}