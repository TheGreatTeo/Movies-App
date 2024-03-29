package com.example.moviesapp.API.tmdbAPI

import com.example.moviesapp.API.tmdbAPI.Credits.CreditsJSON
import com.example.moviesapp.API.tmdbAPI.Genres.GenreJSON
import com.example.moviesapp.API.tmdbAPI.Movies.NowPlayingJSON
import com.example.moviesapp.API.tmdbAPI.Movies.TMDBJSON
import com.example.moviesapp.API.tmdbAPI.Videos.VideosJSON
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TMDBInterface {

    @GET("/3/movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") api_key: String,@Query("page")page: String): Call<TMDBJSON>

    @GET("/3/genre/movie/list")
    fun getGenres(@Query("api_key") api_key: String): Call<GenreJSON>

    @GET("/3/movie/popular")
    fun getPopular(@Query("api_key") api_key: String): Call<TMDBJSON>

    @GET("/3/movie/now_playing")
    fun getNowPlaying(@Query("api_key") api_key: String,@Query("region") region:String = "GB"): Call<NowPlayingJSON>

    @GET("/3/search/movie")
    fun searchMovie(@Query("api_key") api_key: String, @Query("query") query: String): Call<TMDBJSON>

    @GET("/3/movie/{movie_id}/credits")
    fun getCredits(@Path("movie_id") movie_id: String,@Query("api_key") api_key: String): Call<CreditsJSON>


    @GET("/3/movie/{movie_id}/videos")
    fun getTrailer(@Path("movie_id")movie_id: String,@Query("api_key") api_key: String):Call<VideosJSON>

    companion object{
        var BASE_URL = "https://api.themoviedb.org"

        fun create(): TMDBInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(TMDBInterface::class.java)
        }
    }

}