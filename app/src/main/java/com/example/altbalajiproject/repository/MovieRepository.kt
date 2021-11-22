package com.example.altbalajiproject.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.altbalajiproject.model.Movie
import com.example.altbalajiproject.model.MovieInfo
import com.example.altbalajiproject.model.Search
import com.example.altbalajiproject.retrofit.MoviesApi
import com.example.altbalajiproject.room.MovieDatabase
import com.example.altbalajiproject.utils.CheckValidation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MovieRepository(
    private val movieApi:MoviesApi,
    private val movieDatabase:MovieDatabase,
    private val applicationContext: Context)
{

    var movie:List<Search>?=null

    private val movieInfoLiveData = MutableLiveData<ResponseData<MovieInfo>>()
    val movieInfo:LiveData<ResponseData<MovieInfo>>
        get()=movieInfoLiveData


    suspend fun loadPage(topMoviesResponse: MutableLiveData<ResponseData<Any>>, s: String, page : Int) {
        if (CheckValidation.isConnected(applicationContext)){
            movieApi.getMovie(page = page, s = s).enqueue(object : Callback<Movie> {
                override fun onResponse(call: Call<Movie>, response: Response<Movie>) {

                    try
                    {
                        GlobalScope.launch(Dispatchers.IO) {
                            if (response.isSuccessful) {
                                movieDatabase.movieDao().addMovie(response.body()!!.Search)
                                topMoviesResponse.postValue(ResponseData.Success(response.body()))
                            } else {
                                val movie = movieDatabase.movieDao().getMovie()
                                val movieList = Movie("", movie, "", 47)
                                topMoviesResponse.postValue(ResponseData.Success(movieList))
                            }

                        }

                    }
                    catch (e:Exception)
                    {
                        topMoviesResponse.postValue(ResponseData.Error(e.message.toString()))
                    }
                }

                override fun onFailure(call: Call<Movie>, t: Throwable) {

                    topMoviesResponse.postValue(ResponseData.Error(t.message.toString()))

                }
            })
    }
        else
        {
            GlobalScope.launch(Dispatchers.IO) {
                val movie = movieDatabase.movieDao().getMovie()
                val movieList = Movie("", movie, "", 47)
                topMoviesResponse.postValue(ResponseData.Success(movieList))
            }
        }

        }

    suspend fun getMovieInfo(i:String)
    {
        if(CheckValidation.isConnected(applicationContext)){
            val result = movieApi.getMovieInfo(i)
            result.enqueue(object : Callback<MovieInfo> {
                override fun onResponse(call: Call<MovieInfo>, response: Response<MovieInfo>) {
                    Log.d("ROYCODE", response.body().toString())
                    try
                    {
                        GlobalScope.launch(Dispatchers.IO) {

                            if (response?.body() != null) {
                                movieDatabase.movieDao().addMovieInfo(response.body()!!)
                                movieInfoLiveData.postValue(ResponseData.Success(response.body()))
                            } else {

                                val movie = movieDatabase.movieDao().getMovieInfo(i)
                                val movieList = movie
                                movieInfoLiveData.postValue(ResponseData.Success(movieList))
                            }
                        }


                    }
                    catch (e:Exception)
                    {
                        movieInfoLiveData.postValue(ResponseData.Error(e.message.toString()))
                    }
                }
                override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                    movieInfoLiveData.postValue(ResponseData.Error(t.message.toString()))
                }
            });
        }
        else
        {
            val movie = movieDatabase.movieDao().getMovieInfo(i)
            val movieList = movie
            movieInfoLiveData.postValue(ResponseData.Success(movieList))
        }


    }


}