package com.example.moviesapp.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.net.Uri
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
import com.example.moviesapp.data.MovieLibrary
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.json.JSONArray
import java.io.IOException

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    var genrePosition = -1
    var moviePosition = -1
    private var added = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val title: TextView = view.findViewById(R.id.title)
        val rating: TextView = view.findViewById(R.id.rating)
        val description: TextView = view.findViewById(R.id.description)
        val image: ImageView = view.findViewById(R.id.image)
        var addToWatchList: Button = view.findViewById(R.id.addToWatchList)

        genrePosition = arguments?.getInt("genrePosition")!!
        moviePosition = arguments?.getInt("moviePosition")!!

        val movie = readJSON().getJSONArray(genrePosition).getJSONObject(moviePosition)

        title.text = movie.getString("title")
        rating.text = "⭐"+movie.getDouble("rating")
        description.text = movie.getString("description")
        image.setImageURI(Uri.parse(movie.getString("imageURL")))

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userID = currentUser?.uid
        val movieLibrary = MovieLibrary(genrePosition,moviePosition,userID.toString())

        added = false

        FirebaseFirestore.getInstance().collection("movieLibrary").whereEqualTo("userID",userID).get().addOnSuccessListener(
            OnSuccessListener { documents ->
                for(document in documents){
                    Log.d("ADDED",added.toString())
                    if(genrePosition.toString() == document.get("genrePos").toString() && moviePosition.toString() == document.get("moviePos").toString()) {
                        addToWatchList.setBackgroundColor(addToWatchList.context.resources.getColor(R.color.red))
                        addToWatchList.text = "ADDED"
                        added = true
                        Log.d("ADDED",added.toString())
                    }
                }
            })
        addToWatchList.setOnClickListener {
            Log.d("ADDED",added.toString())
            if(!added) {
                FirebaseFirestore.getInstance().collection("movieLibrary").add(movieLibrary)
                    .addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                addToWatchList.setBackgroundColor(addToWatchList.context.resources.getColor(R.color.red))
                                addToWatchList.text = "ADDED"
                                added = true
                            }
                        })
            }
            else{
                var builder = AlertDialog.Builder(activity)
                builder.setMessage("Are you sure you want to remove this movie?")
                    .setPositiveButton("Yes",DialogInterface.OnClickListener{dialog, id ->
                        FirebaseFirestore.getInstance().collection("movieLibrary").whereEqualTo("genrePos",genrePosition).whereEqualTo("moviePos",moviePosition).get().addOnSuccessListener(
                            OnSuccessListener { documents ->
                                val batch = FirebaseFirestore.getInstance().batch()
                                val docs = documents.documents
                                for(doc in docs)
                                    batch.delete(doc.reference)
                                batch.commit()
                            })
                        addToWatchList.setBackgroundColor(addToWatchList.context.resources.getColor(R.color.blue_500))
                        addToWatchList.text = "ADD TO WATCHLIST"
                        added = false
                        dialog.cancel()
                    })
                    .setNegativeButton("No",DialogInterface.OnClickListener{dialog, id -> dialog.cancel() })
                var alert = builder.create()
                alert.show()
            }
        }


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