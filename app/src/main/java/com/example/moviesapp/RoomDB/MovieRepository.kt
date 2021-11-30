package com.example.moviesapp.RoomDB

import androidx.annotation.WorkerThread
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieAndGenre
import com.example.moviesapp.data.MovieItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class MovieRepository(private val movieDao: MovieDao) {

    val allMovies: Flow<List<MovieItem>> = movieDao.getAllMoovies()

    fun getMoviesByGenre(genre: Int): Flow<List<MovieItem>>{
        return movieDao.getMoviesByGenre(genre)
    }
    fun getMovieById(id: Int): MovieAndGenre {
        return movieDao.getMovieById(id)
    }
    fun addMovieToLibrary(movieId: Int){
        return movieDao.addMovieToLibrary(movieId)
    }
    fun removeFromLibrary(movieId: Int){
        return movieDao.removeFromLibrary(movieId)
    }
    fun getAddedMovies(): Flow<List<MovieItem>>{
        return movieDao.getAddedMovies()
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertMovie(movieItem: MovieItem){
        movieDao.insertMovie(movieItem)
    }
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertGenre(genre: Genre){
        movieDao.insertGenre(genre)
    }
}