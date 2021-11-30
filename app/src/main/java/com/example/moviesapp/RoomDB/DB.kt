package com.example.moviesapp.RoomDB

import android.content.Context
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.moviesapp.API.tmdbAPI.GenreJSON
import com.example.moviesapp.API.tmdbAPI.Result
import com.example.moviesapp.API.tmdbAPI.TMDBInterface
import com.example.moviesapp.API.tmdbAPI.TMDBJSON
import com.example.moviesapp.data.Genre
import com.example.moviesapp.data.MovieItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.awaitResponse

@Database(entities = arrayOf(MovieItem::class,Genre::class), version = 1, exportSchema = false)
@TypeConverters(value = arrayOf(ListConvertor::class))
open abstract class DB:RoomDatabase() {

    abstract fun movieDao(): MovieDao

    private class MovieDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback() {
        var tmdbJSON = TMDBJSON(0,listOf<Result>())
        var genreJSON = GenreJSON(listOf())

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {database ->
                scope.launch(Dispatchers.IO) {
                    var movieDao = database.movieDao()
                    populateDatabase(movieDao)
                }
            }
        }
        suspend fun populateDatabase(movieDao: MovieDao) {
            val genres = TMDBInterface.create().getGenres("9df4f48f58d1cb4702a2b4d936029e0d").awaitResponse()
            for(i in 1..469) {
                val movies =
                    TMDBInterface.create().getTopRatedMovies("9df4f48f58d1cb4702a2b4d936029e0d",i.toString())
                        .awaitResponse()

                if (movies.isSuccessful) {
                    tmdbJSON = movies.body()!!
                    Log.d("Genres", tmdbJSON.toString())
                    for (i in tmdbJSON.results) {
                        val movieItem = MovieItem(
                            i.id,
                            i.title,
                            i.overview,
                            i.poster_path,
                            i.vote_average
                        )
                        for (j in i.genre_ids) {
                            if (genres.isSuccessful) {
                                genreJSON = genres.body()!!
                                for (genre in genreJSON.genres)
                                    if (genre.id == j) {
                                        val genre = Genre(0, i.id, j, genre.name)
                                        movieDao.insertGenre(genre)
                                    }
                            }
                        }
                        movieDao.insertMovie(movieItem)
                    }
                }
            }
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: DB? = null

        fun getDatabase(context: Context,scope: CoroutineScope): DB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DB::class.java,
                    "movie_database"
                ).fallbackToDestructiveMigration().addCallback(MovieDatabaseCallback(scope)).build()
                INSTANCE = instance
                instance
            }
        }
    }
}