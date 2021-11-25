package com.example.moviesapp.fragments.MovieFragments

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
import android.widget.ProgressBar
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.moviesapp.R
import com.example.moviesapp.data.MovieLibrary
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import org.json.JSONArray
import java.io.IOException

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    var titleText = ""
    var descriptionText = ""
    var imageURL = ""
    var genreText = ""
    var ratingText = 0.0
    var added: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_details, container, false)
        val title: TextView = view.findViewById(R.id.title)
        val rating: TextView = view.findViewById(R.id.rating)
        val description: TextView = view.findViewById(R.id.synopsis)
        val genreList: ViewPager = view.findViewById(R.id.genreList)
        val castList: ViewPager = view.findViewById(R.id.castList)
        val image: ImageView = view.findViewById(R.id.image)
        var addToWatchList: Button = view.findViewById(R.id.addToWatchList)

        titleText = arguments?.getString("title")!!
        descriptionText = arguments?.getString("description")!!
        imageURL = arguments?.getString("imageURL")!!
        genreText = arguments?.getString("genre")!!
        ratingText = arguments?.getDouble("rating")!!

        title.text = titleText
        rating.text = "Director: " + "| ⭐ " + ratingText
        description.text = descriptionText + descriptionText + descriptionText
        Picasso.get().load("https://image.tmdb.org/t/p/w500"+imageURL).into(image)

        val currentUser = FirebaseAuth.getInstance().currentUser
        val userID = currentUser?.uid
        addToWatchList.setOnClickListener {
            Log.d("ADDED3",added.toString())
            if(!added) {
                lifecycleScope.launch() {
                    //addMovie(movieLibrary)
                    addToWatchList.setBackgroundColor(addToWatchList.context.resources.getColor(R.color.red))
                    addToWatchList.text = "ADDED"
                }
            }
        }


        return view
    }

    suspend fun addMovie(movieLibrary: MovieLibrary){
        return withContext(Dispatchers.IO){
            FirebaseFirestore.getInstance().collection("movieLibrary").add(movieLibrary)
                .addOnCompleteListener(
                    OnCompleteListener { task ->
                        if (task.isSuccessful) {
                            added = true
                        }
                    })
        }
    }

    suspend fun checkMovie(userID: String){
        return withContext(Dispatchers.IO){

        }
    }
}