package com.example.moviesapp.fragments.MovieFragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.ViewPager
import com.example.moviesapp.API.tmdbAPI.*
import com.example.moviesapp.API.tmdbAPI.Credits.CreditsJSON
import com.example.moviesapp.BuildConfig
import com.example.moviesapp.R
import com.example.moviesapp.RoomDB.MovieViewModel
import com.example.moviesapp.RoomDB.MovieViewModelFactory
import com.example.moviesapp.RoomDB.MoviesApplication
import com.example.moviesapp.controller.SharedPrefsHandler
import com.example.moviesapp.controller.ViewPagerAdapter.CastAdapter
import com.example.moviesapp.controller.ViewPagerAdapter.MovieGenreAdapter
import com.example.moviesapp.controller.Callback
import com.example.moviesapp.controller.CheckInternet
import com.example.moviesapp.controller.GoBack
import com.example.moviesapp.data.Cast.Cast
import com.example.moviesapp.data.Cast.CastMember
import com.example.moviesapp.data.Genre.Genre
import com.example.moviesapp.data.Movie.MovieAndGenre
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.awaitResponse

class MovieDetailsFragment : Fragment(R.layout.fragment_movie_details) {

    var movieId = -1
    var callback: Callback? = null
    var added: Boolean = false
    var creditsJSON = CreditsJSON(listOf(),listOf(),0)
    var cast: Cast = Cast("",arrayListOf<CastMember>())
    val sharedPrefs = SharedPrefsHandler()
    val checkInternet = CheckInternet()
    var goBack: GoBack? = null
    private val viewModel: MovieViewModel by viewModels {
        MovieViewModelFactory((requireActivity().application as MoviesApplication).repository)
    }
    lateinit var movieAndGenre: MovieAndGenre
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
        val backArrow: ImageView = view.findViewById(R.id.backArrow)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
        val nestedScrollView: NestedScrollView = view.findViewById(R.id.scrollView)
        val missingData: ImageView = view.findViewById(R.id.missingData)
        var addToWatchList: Button = view.findViewById(R.id.addToWatchList)
        var genreItems = arrayListOf<Genre>()

        progressBar.visibility = View.VISIBLE
        nestedScrollView.visibility = View.GONE
        missingData.visibility = View.GONE
        movieId = arguments?.getInt("id")!!
        Log.d("MovieId",movieId.toString())
        if(checkInternet.isOnline(requireContext())) {
            lifecycleScope.launch() {
                checkMovie()
                movieDetails(movieId.toString())
                title.text = movieAndGenre.movieItem.title
                rating.text = movieAndGenre.movieItem.rating.toString()
                description.text = movieAndGenre.movieItem.description

                for (genres in movieAndGenre.genreIds) {
                    Log.d("GNR", genres.toString())
                    genreItems.add(Genre(-1, -1, genres.genreId, genres.genreName))
                }

                val genreAdapter =
                    MovieGenreAdapter(genreItems, requireContext().applicationContext)
                genreList.adapter = genreAdapter
                genreList.setPadding(0, 0, 600, 0)
                genreList.currentItem = 0
                Picasso.get()
                    .load("https://image.tmdb.org/t/p/w500" + movieAndGenre.movieItem.imageResource)
                    .error(R.drawable.noimage).into(image)
                rating.text = "Director: " + cast!!.director + "  |  " + rating.text + " ⭐ "

                val castAdapter = CastAdapter(cast!!.cast, requireContext().applicationContext)
                castList.adapter = castAdapter
                if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
                    castList.setPadding(0, 0, 610, 0)
                else
                    castList.setPadding(0, 0, 1600, 0)
                if (added) {
                    addToWatchList.setBackgroundColor(resources.getColor(R.color.buttonClicked))
                    addToWatchList.text = "ADDED"
                }

                progressBar.visibility = View.GONE
                nestedScrollView.visibility = View.VISIBLE
            }
        }else{
            progressBar.visibility = View.GONE
            missingData.visibility = View.VISIBLE
            Toast.makeText(requireContext(),"No internet connection!", Toast.LENGTH_SHORT).show()
        }

        addToWatchList.setOnClickListener {
            Log.d("ADDED3", added.toString())
            lifecycleScope.launch() {
                if (!added) {
                    addToWatchList.setBackgroundColor(resources.getColor(R.color.buttonClicked))
                    addToWatchList.text = "ADDED"
                    GlobalScope.async(Dispatchers.IO) {
                        viewModel.addMovieToLibrary(movieId)
                        added = true
                    }.await()
                } else {
                    addToWatchList.setBackgroundColor(resources.getColor(R.color.buttonColor))
                    addToWatchList.text = "ADD TO WATCHLIST"
                    GlobalScope.async(Dispatchers.IO) {
                        viewModel.removeFromLibrary(movieId)
                        added = false
                    }.await()
                }
            }
        }

        backArrow.setOnClickListener {
            Log.d("Clicked","Click!1")
            goBack?.goBack()
        }

        return view
    }

    suspend fun checkMovie(){
        val job = GlobalScope.async(Dispatchers.IO){
            added = viewModel.getMovieById(movieId).movieItem.added
        }.await()
    }

    suspend fun movieDetails(movieID: String){
        return withContext(Dispatchers.IO){
            movieAndGenre = viewModel.getMovieById(movieID.toInt())
            Log.d("MovieAndGenre",movieAndGenre.toString()+" ID:"+ movieID)
            Log.d("movieID",movieID)
            val movies = TMDBInterface.create().getCredits(movieID,BuildConfig.API_KEY).awaitResponse()
            if (movies.isSuccessful) {
                creditsJSON = movies.body()!!
                Log.d("Cast", creditsJSON.toString())
                var castMembers = arrayListOf<CastMember>()
                for (i in creditsJSON.cast) {
                    if(i.known_for_department == "Acting"){
                        castMembers.add(CastMember(i.name,i.character,i.profile_path))
                    }
                }
                for(i in creditsJSON.crew){
                    if(i.job == "Director")
                        Log.d("JOB",i.name)
                        cast.director = i.name
                }
                cast.cast = castMembers
            }else{
                Log.d("Error Body",movies.message())
            }
        }
    }
    fun getGoBack(goBack: GoBack){
        this.goBack = goBack
    }
}