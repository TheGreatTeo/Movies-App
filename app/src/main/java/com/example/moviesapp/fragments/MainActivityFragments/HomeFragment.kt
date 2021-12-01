package com.example.moviesapp.fragments.MainActivityFragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.TMDBJSON
import com.example.moviesapp.R
import com.example.moviesapp.controller.CheckInternet
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.controller.ViewPagerAdapter.Adapter
import com.example.moviesapp.data.MovieItem
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.w3c.dom.Text
import retrofit2.awaitResponse

class HomeFragment : Fragment(R.layout.fragment_home) {
    var popularMovies = ArrayList<MovieItem>()
    var topMovies = ArrayList<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf())
    val checkInternet= CheckInternet()
    val sharedPrefs = SharedPrefsHandler()
    val searchFragment = SearchFragment()
    private lateinit var communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        popularMovies = ArrayList()
        topMovies = ArrayList()
        communicator = activity as Communicator

        val usernameText: TextView = view.findViewById(R.id.helloUser)
        usernameText.text = "Hello " + sharedPrefs.getUsername(requireContext())+"!"

        val searchBar: SearchView = view.findViewById(R.id.searchBar)
        searchBar.setOnClickListener {
            communicator.searchView(searchFragment)
        }
        searchBar.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                communicator.searchView(searchFragment)
            }
        }

        val userIcon: ShapeableImageView = view.findViewById(R.id.userIcon)
        userIcon.setImageResource(R.drawable.usericon)

        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var viewPagerPopular: ViewPager = view.findViewById(R.id.viewPagerPopular)
        val viewPagerTop: ViewPager = view.findViewById(R.id.viewPagerTop)
        val adapterPopular = Adapter(popularMovies,this@HomeFragment.requireContext())
        val adapterTop = Adapter(topMovies,this@HomeFragment.requireContext())
        val nestedScrollView: NestedScrollView = view.findViewById(R.id.scrollView)


        progressBar.visibility = View.VISIBLE
        nestedScrollView.visibility = View.GONE

        if(checkInternet.isOnline(requireContext())) {
            lifecycleScope.launch() {
                getMovies()
                getLatest()
                viewPagerPopular.adapter = adapterPopular
                viewPagerTop.adapter = adapterTop
                if(requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    viewPagerPopular.setPadding(0, 0, 400, 0)
                    viewPagerTop.setPadding(0, 0, 400, 0)
                }else {
                    viewPagerPopular.setPadding(0, 0, 1500, 0)
                    viewPagerTop.setPadding(0, 0, 1500, 0)
                }
                viewPagerPopular.currentItem = 0
                viewPagerTop.currentItem = 0
                delay(250L)
                progressBar.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
            }
        }else{
            Toast.makeText(requireContext(),"No internet connection!", Toast.LENGTH_LONG).show()
            progressBar.visibility = View.GONE
        }

        return view
    }

    suspend fun getMovies() {
        return withContext(Dispatchers.IO) {
            val popular = TMDBInterface.create().getPopular("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            if (popular.isSuccessful) {
                tmdbJSON = popular.body()!!
                Log.d("Popular", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    val movieItem = MovieItem(i.id,i.title,i.overview, i.poster_path,i.vote_average)
                    popularMovies.add(movieItem)
                }
            }else{
                Toast.makeText(requireContext(),"There is something wrong!", Toast.LENGTH_LONG).show()
            }
        }
    }
    suspend fun getLatest() {
        return withContext(Dispatchers.IO) {
            val latest =
                TMDBInterface.create().getTopRatedMovies("9df4f48f58d1cb4702a2b4d936029e0d","1").awaitResponse()
            if (latest.isSuccessful) {
                tmdbJSON = latest.body()!!
                Log.d("Latest", tmdbJSON.toString())
                for (i in tmdbJSON.results) {
                    val movieItem =
                        MovieItem(i.id, i.title, i.overview, i.poster_path, i.vote_average)
                    topMovies.add(movieItem)
                }
            } else {
                Toast.makeText(requireContext(), "There is something wrong!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}