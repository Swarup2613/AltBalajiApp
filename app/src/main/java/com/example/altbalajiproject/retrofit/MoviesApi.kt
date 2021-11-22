package com.example.altbalajiproject.retrofit

import com.example.altbalajiproject.model.Movie
import com.example.altbalajiproject.model.MovieInfo
import com.example.altbalajiproject.model.Search
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val BASE_URL="http://www.omdbapi.com/"
const val API_KEY="e5311742"

interface MoviesApi {
    @GET("?apikey=$API_KEY")
     fun getMovie(@Query("s") s: String,@Query("page") page: Int): Call<Movie>

    @GET("?apikey=$API_KEY")
     fun getMovieInfo(@Query("i") i: String): Call<MovieInfo>

}
    object MovieService
    {
        val movieInstance:MoviesApi

        init {
            val retrofit= Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            movieInstance=retrofit.create(MoviesApi::class.java)

        }
    }

