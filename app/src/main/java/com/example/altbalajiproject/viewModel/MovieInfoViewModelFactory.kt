package com.example.altbalajiproject.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.altbalajiproject.repository.MovieRepository

class MovieInfoViewModelFactory(private val movieRepository: MovieRepository, private val i:String):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieInfoViewModel(movieRepository,i) as T
    }
}