package com.example.moviesapp.API.tmdbAPI

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBInterface {

    @GET("/3/movie/top_rated")
    fun getTopRatedMovies(@Query("api_key") api_key: String): Call<TMDBJSON>

    @GET("/3/genre/movie/list")
    fun getGenres(@Query("api_key") api_key: String): Call<GenreJSON>

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