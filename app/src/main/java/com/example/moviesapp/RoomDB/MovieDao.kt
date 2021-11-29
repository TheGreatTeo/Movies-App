package com.example.moviesapp.RoomDB

import androidx.room.*
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.MovieAndGenre
import com.example.moviesapp.data.MovieItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {

    @Query("SELECT * FROM movie_table ORDER BY id ASC")
    fun getAllMoovies(): Flow<List<MovieItem>>

    @Query("SELECT * FROM movie_table WHERE id=:id")
    fun getMovieById(id: Int): MovieAndGenre

    @Transaction
    @Query("SELECT * FROM movie_table")
    fun getAllMoviesWithGenres(): Flow<List<MovieAndGenre>>


    @Query("SELECT * FROM movie_table,Genre WHERE id=movieId AND genreId =:genre ORDER BY id ASC")
    fun getMoviesByGenre(genre: Int): Flow<List<MovieItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieItem: MovieItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genre: Genre)

}