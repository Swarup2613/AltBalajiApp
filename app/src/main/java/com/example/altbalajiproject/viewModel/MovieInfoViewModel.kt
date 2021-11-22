package com.example.altbalajiproject.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.altbalajiproject.model.MovieInfo
import com.example.altbalajiproject.repository.MovieRepository
import com.example.altbalajiproject.repository.ResponseData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.internal.publicsuffix.PublicSuffixDatabase.get

class MovieInfoViewModel(private val movieRepository: MovieRepository, private val i:String): ViewModel() {

    init {

        viewModelScope.launch(Dispatchers.IO) {
            movieRepository.getMovieInfo(i)
        }
    }

    val movieInfo: LiveData<ResponseData<MovieInfo>>
    get()=movieRepository.movieInfo

}