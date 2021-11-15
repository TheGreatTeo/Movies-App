package com.example.moviesapp.API

import com.example.moviesapp.controller.ApiInterface
import com.example.moviesapp.data.Movie
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface TMDBInterface {


    @GET("movie/top_rated")
    fun getMovies(@Query("api_key")api_key: String): Call<ArrayList<Movie>>

    companion object{
        var BASE_URL = "https://api.themoviedb.org/3/"

        fun create() : ApiInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(ApiInterface::class.java)
        }
    }
}