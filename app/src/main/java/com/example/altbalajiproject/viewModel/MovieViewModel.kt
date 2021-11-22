package com.example.altbalajiproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.altbalajiproject.repository.MovieRepository
import com.example.altbalajiproject.repository.ResponseData

class MovieViewModel(private val repository: MovieRepository):ViewModel() {






    private var _topMoviesFirstPageResponse = MutableLiveData<ResponseData<Any>>()
    private var _topMoviesNextPageResponse = MutableLiveData<ResponseData<Any>>()


     suspend fun requestFirstPageTopMovies(s:String, page : Int) {
        repository.loadPage(_topMoviesFirstPageResponse ,s,page)
    }

     suspend fun requestFirstNextPageMovies(s:String, page : Int) {
        repository.loadPage(_topMoviesNextPageResponse ,s,page)
    }

    val topMoviesFirstPageResponse : LiveData<ResponseData<Any>>
        get() = _topMoviesFirstPageResponse
    val topMoviesNextPageResponse : LiveData<ResponseData<Any>>
        get() = _topMoviesNextPageResponse

}