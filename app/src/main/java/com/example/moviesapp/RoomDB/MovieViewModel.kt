package com.example.moviesapp.RoomDB

import androidx.lifecycle.*
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.MovieItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class MovieViewModel(private val repository: MovieRepository): ViewModel() {


    val allMovies: LiveData<List<MovieItem>> = repository.allMovies.asLiveData()

    fun getMovieByGenre(genre: Int) = repository.getMoviesByGenre(genre).asLiveData()

    fun getMovieById(id: Int) = repository.getMovieById(id)

    fun addMovieToLibrary(movieId: Int) = repository.addMovieToLibrary(movieId)

    fun removeFromLibrary(movieId: Int) = repository.removeFromLibrary(movieId)

    val addedMovies = repository.getAddedMovies().asLiveData()

    fun insertMovie(movieItem: MovieItem) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertMovie(movieItem)
    }
    fun insertGenre(genre: Genre) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertGenre(genre)
    }
    fun movieExists(movieId: Int) = repository.movieExists(movieId)
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