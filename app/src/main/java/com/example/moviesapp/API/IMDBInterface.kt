package com.example.moviesapp.API

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface IMDBInterface {

    @GET("/en/API/SearchTitle/{api_key}/{title}")
    fun searchTitle(@Path("api_key") api_key: String, @Path("title") title: String): Call<TitleJSON>

    companion object{
        var BASE_URL = "https://imdb-api.com"

        fun create(): IMDBInterface {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(IMDBInterface::class.java)
        }
    }
}