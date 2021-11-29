package com.example.moviesapp.RoomDB

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class MoviesApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob())

    val database by lazy { DB.getDatabase(this,applicationScope) }
    val repository by lazy { MovieRepository(database.movieDao()) }
}