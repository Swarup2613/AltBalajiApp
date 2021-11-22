package com.example.altbalajiproject.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.altbalajiproject.utils.MovieApplication
import com.example.altbalajiproject.R
import com.example.altbalajiproject.adapter.MoviesAdapter
import com.example.altbalajiproject.databinding.ActivityMainBinding

import com.example.altbalajiproject.model.Movie
import com.example.altbalajiproject.model.Search
import com.example.altbalajiproject.repository.ResponseData

import com.example.altbalajiproject.utils.CheckValidation
import com.example.altbalajiproject.utils.PaginationScrollListener
import com.example.altbalajiproject.viewModel.MovieViewModel
import com.example.altbalajiproject.viewModel.MovieViewModelFacctory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeoutException

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MovieViewModel
    lateinit var binding: ActivityMainBinding
    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var movieName:String="batman"
    private var currentPage: Int = pageStart
    lateinit var adapter: MoviesAdapter
    lateinit var movie :List<Search>


    override  fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity=this
        val repository = (application as MovieApplication).movieRepository
        viewModel=ViewModelProvider(this,MovieViewModelFacctory(repository)).get(MovieViewModel::class.java)
        initRecyclerView()
        ObserverDatarequest()


    }
    @DelicateCoroutinesApi
    private  fun initRecyclerView() {

            adapter= MoviesAdapter(this@MainActivity)
            binding.adapterTopMovies=adapter
            binding.recyclerView.setHasFixedSize(true)
            binding.recyclerView.itemAnimator=DefaultItemAnimator()
           // layManager = LinearLayoutManager(this@MainActivity)
           // binding.recyclerView.layoutManager = layManager

        GlobalScope.launch(Dispatchers.IO) {
            loadFirstPage()
        }



             binding.recyclerView.addOnScrollListener(object :PaginationScrollListener(binding.recyclerView.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                Handler(Looper.myLooper()!!).postDelayed({
                    GlobalScope.launch(Dispatchers.IO) {
                        loadNextPage()
                    }

                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })


    }


    private suspend fun loadFirstPage() {
        hideErrorView()
            viewModel.requestFirstPageTopMovies(movieName,currentPage)

    }

    suspend fun loadNextPage() {
            viewModel.requestFirstNextPageMovies(movieName,currentPage)
    }



    private fun ObserverDatarequest() {


        viewModel.topMoviesFirstPageResponse.observe(this) {


                when(it) {
                    is ResponseData.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is ResponseData.Success -> {

                        it.data?.let {
                            if (it is Movie) {
                                binding.progressBar.visibility = View.GONE
                                hideErrorView()
                                Log.d("RoyCode", it.Search.toString())
                                val results: MutableList<Search> =
                                    fetchResults(it) as MutableList<Search>
                                adapter.addAll(results)
                                binding.progressBar.visibility = View.GONE

                                totalPages = it.totalPages

                                if (currentPage <= totalPages) adapter.addLoadingFooter()
                                else isLastPage = true

                            } else if (it is Throwable) {
                                showErrorView(it)
                            } else {
                                Log.d("TAG_TEST", "Error First Page")
                            }


                        }
                    }
                    is ResponseData.Error->{
                        binding.progressBar.visibility = View.GONE
                        it.errorMessage
                        Toast.makeText(this,it.errorMessage.toString(),Toast.LENGTH_SHORT).show()
                    }
                }

        }

        viewModel.topMoviesNextPageResponse.observe(this) {

            when(it){
                is ResponseData.Loading->{
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResponseData.Success->{
                    it.data?.let {

                        if (it is Movie) {
                            binding.progressBar.visibility = View.GONE
                            val results = fetchResults(it) as MutableList<Search>
                            adapter.removeLoadingFooter()
                            isLoading = false
                            adapter.addAll(results)

                            if (currentPage != totalPages) adapter.addLoadingFooter()
                            else isLastPage = true

                        }else if (it is Throwable){
                            adapter.showRetry(true, fetchErrorMessage(it))
                        }else{
                            Log.d("TAG_TEST" , "Error First Page")
                        }

                    }


                }
                is ResponseData.Error->{
                    it.errorMessage
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this,it.errorMessage.toString(), Toast.LENGTH_SHORT).show()


                }
            }


        }




    }

    private fun fetchResults(moviesTopModel: Movie): List<Search>? {
        return moviesTopModel.Search
    }


    private fun showErrorView(throwable: Throwable?) {
        if (binding.lyError.errorLayout.visibility == View.GONE) {
            binding.lyError.errorLayout.visibility = View.VISIBLE
            binding.progressBar.visibility = View.GONE

            if (!CheckValidation.isConnected(this)) {
                binding.lyError.errorTxtCause.setText("Error")
            } else {
                if (throwable is TimeoutException) {
                    binding.lyError.errorTxtCause.setText("timeout")
                } else {
                    binding.lyError.errorTxtCause.setText("Error")
                }
            }
        }
    }


    private fun hideErrorView() {
        if (binding.lyError.errorLayout.visibility == View.VISIBLE) {
            binding.lyError.errorLayout.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE
        }
    }

    private fun fetchErrorMessage(throwable: Throwable?): String {
        var errorMsg = "Error"

        if (!CheckValidation.isConnected(this)) {
            errorMsg = "No Internet"
        } else if (throwable is TimeoutException) {
            errorMsg = "timeout"
        }

        return errorMsg
    }




}

