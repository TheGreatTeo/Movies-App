package com.example.moviesapp.fragments.MainActivityFragments

import com.example.moviesapp.controller.RecyclerViewAdapters.GenreAdapter
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.API.tmdbAPI.Genres.Genre
import com.example.moviesapp.API.tmdbAPI.Genres.GenreJSON
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.controller.Callback
import com.example.moviesapp.controller.CheckInternet
import com.example.moviesapp.controller.Communicator
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.data.Genre.GenreItem
import com.example.moviesapp.fragments.MovieFragments.MovieFragment
import kotlinx.coroutines.*
import retrofit2.awaitResponse

class DashboardFragment : Fragment(R.layout.fragment_dashboard), GenreAdapter.OnItemClickListener,Callback {

    var callbackFragment:Callback? = null
    private lateinit var communicator: Communicator
    val sharedPrefs = SharedPrefsHandler()
    private val movieFragment = MovieFragment()
    var genres = GenreJSON(listOf<Genre>())
    var genreList = ArrayList<GenreItem>()
    val checkInternet= CheckInternet()
    val hashMap: Map<String,Int> = hashMapOf(
        "Action" to R.drawable.action,
        "Adventure" to R.drawable.adventure,
        "Animation" to R.drawable.animation,
        "Comedy" to R.drawable.comedy,
        "Crime" to R.drawable.crime,
        "Documentary" to R.drawable.documentary,
        "Drama" to R.drawable.drama,
        "Family" to R.drawable.family,
        "Fantasy" to R.drawable.fantasy,
        "History" to R.drawable.history,
        "Horror" to R.drawable.horror,
        "Music" to R.drawable.music,
        "Mystery" to R.drawable.mystery,
        "Romance" to R.drawable.romance,
        "Science Fiction" to R.drawable.science_fiction,
        "Thriller" to R.drawable.thriller,
        "TV Movie" to R.drawable.tv_movie,
        "War" to R.drawable.war,
        "Western" to R.drawable.western
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        var recyclerView: RecyclerView = view.findViewById(R.id.genreList)
        val missingData: ImageView = view.findViewById(R.id.missingData)
        communicator = activity as Communicator

        progressBar.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        missingData.visibility = View.GONE

        genreList = ArrayList()

        var genreSet = sharedPrefs.getGenresName(requireActivity())
        if(checkInternet.isOnline(requireContext())) {
            if (genreSet.size == 1) {
                lifecycleScope.launch() {
                    Log.d("GETGenre", "GET")
                    getGenres()
                    delay(500L)
                    recyclerView.adapter = GenreAdapter(genreList, this@DashboardFragment)
                    recyclerView.layoutManager =
                        LinearLayoutManager(requireContext().applicationContext)
                    recyclerView.setHasFixedSize(true)
                    progressBar.visibility = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            } else {
                lifecycleScope.launch() {
                    Log.d("GETGenre", "NOGET")
                    for (i in genreSet) {
                        val genreItem = GenreItem(i, hashMap.get(i)!!)
                        genreList.add(genreItem)
                        recyclerView.adapter = GenreAdapter(genreList, this@DashboardFragment)
                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext().applicationContext)
                        recyclerView.setHasFixedSize(true)
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }else{
            if (genreSet.size == 1) {
                progressBar.visibility = View.GONE
                missingData.visibility = View.VISIBLE
            } else {
                lifecycleScope.launch() {
                    Log.d("GETGenre", "NOGET")
                    for (i in genreSet) {
                        val genreItem = GenreItem(i, hashMap.get(i)!!)
                        genreList.add(genreItem)
                        recyclerView.adapter = GenreAdapter(genreList, this@DashboardFragment)
                        recyclerView.layoutManager =
                            LinearLayoutManager(requireContext().applicationContext)
                        recyclerView.setHasFixedSize(true)
                        progressBar.visibility = View.GONE
                        recyclerView.visibility = View.VISIBLE
                    }
                }
            }
        }
        return view
    }
    override fun onItemClick(position: Int) {
        val genresId = sharedPrefs.getGenresId(requireActivity())
        val genresName = sharedPrefs.getGenresName(requireActivity())
        movieFragment.getCallback(this)
        communicator.passGenre(movieFragment, genresId.get(position).toInt(),genresName.get(position))
        }

    suspend fun getGenres() {
        return withContext(Dispatchers.IO) {
            val genre =
                    TMDBInterface.create().getGenres(BuildConfig.API_KEY).awaitResponse()
            var genresId = mutableListOf<String>()
            var genresName = mutableListOf<String>()
            if (genre.isSuccessful) {
                genres = genre.body()!!
                Log.d("Genres", genres.toString())
                for (i in 0 until genres.genres.size) {
                    val genre = genres.genres.get(i).name
                    Log.d("genreList",genre + " " + genres.genres[i].id)
                    val genreItem = GenreItem(genre, hashMap.getValue(genre))
                    genreList.add(genreItem)
                    genresId.add(genres.genres[i].id.toString())
                    genresName.add(genres.genres[i].name)
                }
                Log.d("genres",genresId.toString() + "\n" + genresName.toString())
                sharedPrefs.setGenres(requireActivity(),genresId,genresName)
            }
        }
    }

    override fun changeFragment() {
        Log.d("Clicked","Click!3")
        callbackFragment?.changeFragment()
    }

    fun getCallback(callback: Callback){
        this.callbackFragment = callback
    }
}