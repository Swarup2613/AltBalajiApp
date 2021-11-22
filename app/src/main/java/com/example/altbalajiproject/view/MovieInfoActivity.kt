package com.example.altbalajiproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.altbalajiproject.utils.MovieApplication
import com.example.altbalajiproject.R
import com.example.altbalajiproject.databinding.ActivityMovieInfoBinding
import com.example.altbalajiproject.repository.ResponseData
import com.example.altbalajiproject.viewModel.MovieInfoViewModel
import com.example.altbalajiproject.viewModel.MovieInfoViewModelFactory
import java.lang.Error

class MovieInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityMovieInfoBinding
    lateinit var viewModel:MovieInfoViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_info)
        initilizeVIewModel()


    }

    private fun initilizeVIewModel() {

        val imdbValue=intent.getStringExtra("imdbID")
        val posterValue= intent.getStringExtra("poster").toString()
        Glide.with(this).load(posterValue).into(binding.ivPoster1)
        val repository = (application as MovieApplication).movieRepository

        viewModel = ViewModelProvider(this, MovieInfoViewModelFactory(repository,imdbValue.toString())
        ).get(MovieInfoViewModel::class.java)

        viewModel.movieInfo.observe(this){

            when(it)
            {
                is ResponseData.Loading->{
                    binding.progressBar4.visibility=View.VISIBLE
                }

                is ResponseData.Success->{
                    it.data?.let {

                        binding.progressBar4.visibility=View.GONE
                        binding.movieInfo=it
                    }

                }

                is ResponseData.Error->{
                    it.errorMessage
                    binding.progressBar4.visibility=View.GONE
                    Toast.makeText(this,it.errorMessage.toString(), Toast.LENGTH_SHORT).show()

                }
            }

        }
       // binding.movieInfoViewModel=viewModel
  /*      binding.lifecycleOwner=this*/

    }

    override fun onStop() {
        super.onStop()
        finish()
    }
}