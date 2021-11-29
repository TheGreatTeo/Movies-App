package com.example.moviesapp.RoomDB

import android.app.Application
import androidx.lifecycle.*
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.Movie
import com.example.moviesapp.data.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MovieViewModel(private val repository: MovieRepository): ViewModel() {


    val allMovies: LiveData<List<MovieItem>> = repository.allMovies.asLiveData()

    fun getMovieByGenre(genre: Int) = repository.getMoviesByGenre(genre).asLiveData()
    fun getMovieById(id: Int) = repository.getMovieById(id)

    fun insertMovie(movieItem: MovieItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMovie(movieItem)
    }
    fun insertGenre(genre: Genre) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertGenre(genre)
    }
}

class MovieViewModelFactory(private val repository: MovieRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MovieViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}