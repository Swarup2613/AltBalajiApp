package com.example.altbalajiproject.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.altbalajiproject.model.Movie
import com.example.altbalajiproject.model.MovieInfo
import com.example.altbalajiproject.model.Search

@Dao
interface MovieDAO {

        @Insert
        suspend fun addMovie(movie: List<Search>?=null)

        @Query("SELECT * FROM movie")
         suspend fun getMovie():List<Search>


        @Insert
        suspend fun addMovieInfo(movie: MovieInfo?=null)

        @Query("SELECT * FROM movie_info WHERE imdbID LIKE:imdbID")
        suspend fun getMovieInfo(imdbID:String?):MovieInfo

}