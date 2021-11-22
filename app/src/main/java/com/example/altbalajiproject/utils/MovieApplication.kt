package com.example.altbalajiproject.utils

import android.app.Application
import com.example.altbalajiproject.repository.MovieRepository
import com.example.altbalajiproject.retrofit.MovieService
import com.example.altbalajiproject.room.MovieDatabase
import retrofit2.Retrofit

class MovieApplication: Application() {

    lateinit var movieRepository:MovieRepository
    lateinit var database: MovieDatabase

    override fun onCreate() {
        super.onCreate()

        initialize()
    }
    private fun initialize() {

        val movieApi=MovieService.movieInstance
         database=MovieDatabase.getDatabase(applicationContext)
        movieRepository=MovieRepository(movieApi,database,applicationContext)
    }
}