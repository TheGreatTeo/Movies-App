package com.example.moviesapp.fragments.MainActivityFragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.moviesapp.API.tmdbAPI.Movies.Dates
import com.example.moviesapp.API.tmdbAPI.Movies.NowPlayingJSON
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.Movies.TMDBJSON
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.controller.CheckInternet
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.controller.ViewPagerAdapter.Adapter
import com.example.moviesapp.data.Movie.MovieItem
import com.example.moviesapp.fragments.MovieFragments.MovieDetailsFragment
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse

class HomeFragment : Fragment(R.layout.fragment_home),Adapter.OnItemClickListener {
    var popularMovies = arrayListOf<MovieItem>()
    var nowPlayingMovies = arrayListOf<MovieItem>()
    var topMovies = arrayListOf<MovieItem>()
    var tmdbJSON = TMDBJSON(0,listOf())
    var nowPlayingJSON = NowPlayingJSON(Dates("",""),0,listOf())
    val checkInternet= CheckInternet()
    val sharedPrefs = SharedPrefsHandler()
    val searchFragment = SearchFragment()
    val movieDetailsFragment = MovieDetailsFragment()
    private lateinit var communicator: Communicator
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        popularMovies = arrayListOf()
        nowPlayingMovies = arrayListOf()
        topMovies = arrayListOf()
        communicator = activity as Communicator

        val missingData: ImageView = view.findViewById(R.id.missingData)
        val popularText: TextView = view.findViewById(R.id.latestText)
        val nowText: TextView = view.findViewById(R.id.nowText)
        val topRated: TextView = view.findViewById(R.id.topRated)


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
        val viewPagerNow: ViewPager = view.findViewById(R.id.viewPagerNow)
        val viewPagerTop: ViewPager = view.findViewById(R.id.viewPagerTop)

        val adapterPopular = Adapter(popularMovies,requireContext(),this@HomeFragment)
        val adapterTop = Adapter(topMovies,requireContext(),this@HomeFragment)
        val adapterNow = Adapter(nowPlayingMovies,requireContext(),this@HomeFragment)

        val nestedScrollView: NestedScrollView = view.findViewById(R.id.scrollView)


        progressBar.visibility = View.VISIBLE
        missingData.visibility = View.GONE
        nestedScrollView.visibility = View.GONE

        if(checkInternet.isOnline(requireContext())) {
            lifecycleScope.launch() {
                getMovies()
                getLatest()
                getNowPlaying()
                viewPagerPopular.adapter = adapterPopular
                viewPagerTop.adapter = adapterTop
                viewPagerNow.adapter = adapterNow
                if(requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    viewPagerPopular.setPadding(0, 0, 400, 0)
                    viewPagerNow.setPadding(0, 0, 400, 0)
                    viewPagerTop.setPadding(0, 0, 400, 0)
                }else {
                    viewPagerPopular.setPadding(0, 0, 1410, 0)
                    viewPagerNow.setPadding(0, 0, 1410, 0)
                    viewPagerTop.setPadding(0, 0, 1410, 0)
                }
                viewPagerPopular.currentItem = 0
                viewPagerNow.currentItem = 0
                viewPagerTop.currentItem = 0
                progressBar.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
            }
        }else{
            Toast.makeText(requireContext(),"No internet connection!", Toast.LENGTH_LONG).show()
            nestedScrollView.visibility = View.VISIBLE
            missingData.visibility = View.VISIBLE
            nowText.alpha = 0F
            popularText.alpha = 0F
            topRated.alpha = 0F
            viewPagerNow.alpha = 0F
            viewPagerPopular.alpha = 0F
            viewPagerTop.alpha = 0F
            progressBar.visibility = View.GONE
        }

        return view
    }

    suspend fun getMovies() {
        return withContext(Dispatchers.IO) {
            val popular = TMDBInterface.create().getPopular(BuildConfig.API_KEY).awaitResponse()
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
                TMDBInterface.create().getTopRatedMovies(BuildConfig.API_KEY,"1").awaitResponse()
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
    suspend fun getNowPlaying(){
        return withContext(Dispatchers.IO){
            val nowPlaying = TMDBInterface.create().getNowPlaying(BuildConfig.API_KEY).awaitResponse()
            if(nowPlaying.isSuccessful){
                nowPlayingJSON = nowPlaying.body()!!
                for(i in nowPlayingJSON.results){
                    val movieItem = MovieItem(i.id,i.title,i.overview,i.poster_path,i.vote_average)
                    nowPlayingMovies.add(movieItem)
                }
            }else{
                Toast.makeText(requireContext(), "There is something wrong!", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    override fun onItemClick(position: Int) {
    }

//    suspend fun checkMovie(movieItem: MovieItem){
//        lifecycleScope.async(Dispatchers.IO) {
//            if (!viewModel.movieExists(movieItem.id)) {
//                viewModel.insertMovie(movieItem)
//                for (i in genresList) {
//                    if (i.key == movieItem.id) {
//                        for (gnr in i.value) {
//                            val genre = Genre(0, movieItem.id, gnr.id, gnr.name)
//                            viewModel.insertGenre(genre)
//                        }
//                    }
//                }
//            }
//        }.await()
//    }
}