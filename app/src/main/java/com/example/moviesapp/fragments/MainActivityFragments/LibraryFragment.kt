package com.example.moviesapp.fragments.MainActivityFragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.R
import com.example.moviesapp.RoomDB.MovieViewModel
import com.example.moviesapp.RoomDB.MovieViewModelFactory
import com.example.moviesapp.RoomDB.MoviesApplication
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieAdapter
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieListAdapter
import com.example.moviesapp.data.MovieItem
import com.example.moviesapp.fragments.MovieFragments.MovieDetailsFragment
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.io.IOException

class LibraryFragment : Fragment(R.layout.fragment_library), MovieListAdapter.OnItemClickListener {
    private lateinit var communicator: Communicator
    var position: Int = -1
    var movieList = ArrayList<MovieItem>()
    var movieDetailsFragment = MovieDetailsFragment()
    val userID = FirebaseAuth.getInstance().currentUser?.uid
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)

        communicator = activity as Communicator
        movieList = ArrayList<MovieItem>()
        var recyclerView: RecyclerView = view.findViewById(R.id.movieLibraryList)
        val adapter = MovieListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.addedMovies.observe(viewLifecycleOwner){ movies ->
            adapter.setMovies(movies)
        }
        var noContent: TextView = view.findViewById(R.id.noContent)



        return view
    }


    override fun onItemClick(position: Int) {
        FirebaseFirestore.getInstance().collection("movieLibrary").whereEqualTo("userID",userID).get().addOnSuccessListener(
            OnSuccessListener { documents ->
                for(document in documents){
                    //communicator.passMovie(movieDetailsFragment,"","","","",0.0)
                    return@OnSuccessListener
                }
            })

    }
}