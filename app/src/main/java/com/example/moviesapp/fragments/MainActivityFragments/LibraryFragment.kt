package com.example.moviesapp.fragments.MainActivityFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.data.MovieItem
import com.example.moviesapp.fragments.MovieFragments.MovieDetailsFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import org.json.JSONArray
import java.io.IOException

class LibraryFragment : Fragment(R.layout.fragment_library), MovieAdapter.OnItemClickListener {
    private lateinit var communicator: Communicator
    var position: Int = -1
    var movieList = ArrayList<MovieItem>()
    var movieDetailsFragment = MovieDetailsFragment()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        communicator = activity as Communicator
        movieList = ArrayList<MovieItem>()
        var recyclerView: RecyclerView = view.findViewById(R.id.movieLibraryList)
        var noContent: TextView = view.findViewById(R.id.noContent)

        FirebaseFirestore.getInstance().collection("movieLibrary").whereEqualTo("userID",userID).get().addOnSuccessListener(
            OnSuccessListener { documents ->
                for(document in documents){
                    noContent.visibility = View.INVISIBLE
                    Log.d("Docu",document.id)
                    val jsonObject = readJSON().getJSONArray(document.getLong("genrePos")!!.toInt()).getJSONObject(document.getLong("moviePos")!!.toInt())
                    val movieItem = MovieItem("","",Picasso.get().load("https://image.tmdb.org/t/p/w500"),"",jsonObject.getDouble("rating"),"")
                    movieList.add(movieItem)

                    recyclerView.adapter = MovieAdapter(movieList,this)
                    recyclerView.layoutManager = LinearLayoutManager(requireContext().applicationContext)
                    recyclerView.setHasFixedSize(true)
                }
            }).addOnFailureListener { noContent.visibility = View.VISIBLE }


        return view
    }

    private fun readJSON() : JSONArray {
        var json: String? = null
        var jsonArray: JSONArray = JSONArray()
        try{
            val inputStream = resources.openRawResource(resources.getIdentifier("movies","raw",requireContext().applicationContext.packageName))
            json = inputStream.bufferedReader().use { it.readText() }

            jsonArray = JSONArray(json)
        }catch (e : IOException){
            Log.d("IOException",e.printStackTrace().toString())
        }
        return jsonArray
    }

    override fun onItemClick(position: Int) {
        FirebaseFirestore.getInstance().collection("movieLibrary").whereEqualTo("userID",userID).get().addOnSuccessListener(
            OnSuccessListener { documents ->
                for(document in documents){
                    communicator.passMovie(movieDetailsFragment,"","","","",0.0)
                    return@OnSuccessListener
                }
            })

    }
}