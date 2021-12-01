package com.example.moviesapp.RoomDB

import androidx.annotation.WorkerThread
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.MovieAndGenre
import com.example.moviesapp.data.MovieItem
import kotlinx.coroutines.flow.Flow

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

    fun movieExists(movieId: Int): Boolean{
        return movieDao.movieExists(movieId)
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