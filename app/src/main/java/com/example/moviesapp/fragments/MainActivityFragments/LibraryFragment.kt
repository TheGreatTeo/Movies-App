package com.example.moviesapp.fragments.MainActivityFragments

import android.os.Bundle
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
import com.example.moviesapp.controller.RecyclerViewAdapters.MovieListAdapter
import com.example.moviesapp.data.MovieItem
import com.example.moviesapp.fragments.MovieFragments.MovieDetailsFragment

class LibraryFragment : Fragment(R.layout.fragment_library), MovieListAdapter.OnItemClickListener {
    private lateinit var communicator: Communicator
    var position: Int = -1
    var movieList = listOf<MovieItem>()
    var movieDetailsFragment = MovieDetailsFragment()
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_library, container, false)
        var noContent: TextView = view.findViewById(R.id.noContent)
        communicator = activity as Communicator
        movieList = listOf()
        var recyclerView: RecyclerView = view.findViewById(R.id.movieLibraryList)
        val adapter = MovieListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        viewModel.addedMovies.observe(viewLifecycleOwner){ movies ->
            if(!movies.isEmpty())
                noContent.visibility = View.GONE
            else
                noContent.visibility = View.VISIBLE
            adapter.setMovies(movies)
            movieList = movies
        }



        return view
    }


    override fun onItemClick(position: Int) {
        val movieItem = movieList.get(position)
        communicator.passMovie(movieDetailsFragment,movieItem.id)
    }
}