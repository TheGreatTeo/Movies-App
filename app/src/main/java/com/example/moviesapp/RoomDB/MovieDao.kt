package com.example.moviesapp.RoomDB

import androidx.room.*
import com.example.moviesapp.data.Genre.Genre
import com.example.moviesapp.data.Movie.MovieAndGenre
import com.example.moviesapp.data.Movie.MovieItem
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

    @Query("UPDATE movie_table SET added=1 WHERE id=:movieId AND added=0")
    fun addMovieToLibrary(movieId: Int)

    @Query("UPDATE movie_table SET added=0 WHERE id=:movieId AND added=1")
    fun removeFromLibrary(movieId: Int)

    @Query("SELECT * FROM movie_table WHERE added=1")
    fun getAddedMovies(): Flow<List<MovieItem>>

    @Query("SELECT * FROM movie_table,Genre WHERE id=movieId AND genreId =:genre ORDER BY id ASC")
    fun getMoviesByGenre(genre: Int): Flow<List<MovieItem>>

    @Query("SELECT * FROM movie_table WHERE id=:movieId")
    fun movieExists(movieId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMovie(movieItem: MovieItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertGenre(genre: Genre)

}