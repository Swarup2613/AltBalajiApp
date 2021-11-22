package com.example.altbalajiproject.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.example.altbalajiproject.R
import com.example.altbalajiproject.databinding.ItemLoadingBinding
import com.example.altbalajiproject.databinding.MovieItemBinding

import com.example.altbalajiproject.model.Search
import com.example.altbalajiproject.utils.PaginationAdapterCallback
import com.example.altbalajiproject.view.MainActivity
import com.example.altbalajiproject.view.MovieInfoActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by ( Eng Ali Al Fayed)
 * Class do :
 * Date 12/28/2020 - 3:12 PM
 */
class MoviesAdapter(private var mActivity: MainActivity) : RecyclerView.Adapter<RecyclerView.ViewHolder>() ,
    PaginationAdapterCallback {

    private val item: Int = 0
    private val loading: Int = 1

    private var isLoadingAdded: Boolean = false
    private var retryPageLoad: Boolean = false

    private var errorMsg: String? = ""

    private var moviesModels: MutableList<Search> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  if(viewType == item){
            val binding: MovieItemBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.movie_item, parent, false)
            TopMoviesVH(binding)
        }else{
            val binding: ItemLoadingBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.item_loading, parent, false)
            LoadingVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = moviesModels[position]
        if(getItemViewType(position) == item){
            val myOrderVH: TopMoviesVH = holder as TopMoviesVH
            myOrderVH.bind(model)
                myOrderVH.itemRowBinding.cardView.setOnClickListener {
                    val intent=Intent(mActivity,MovieInfoActivity::class.java)
                    intent.putExtra("poster",model.Poster)
                    intent.putExtra("imdbID",model.imdbID)
                    mActivity.startActivity(intent)
                }

        }else{
            val loadingVH: LoadingVH = holder as LoadingVH
            if (retryPageLoad) {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.VISIBLE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.GONE

                if(errorMsg != null) loadingVH.itemRowBinding.loadmoreErrortxt.text = errorMsg
                else loadingVH.itemRowBinding.loadmoreErrortxt.text = "Error"

            } else {
                loadingVH.itemRowBinding.loadmoreErrorlayout.visibility = View.GONE
                loadingVH.itemRowBinding.loadmoreProgress.visibility = View.VISIBLE
            }

            loadingVH.itemRowBinding.loadmoreRetry.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }
            loadingVH.itemRowBinding.loadmoreErrorlayout.setOnClickListener{
                showRetry(false, "")
                retryPageLoad()
            }




        }
    }

    override fun getItemCount(): Int {
        return if (moviesModels.size > 0) moviesModels.size else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            item
        }else {
            if (position == moviesModels.size - 1 && isLoadingAdded) {
                loading
            } else {
                item
            }
        }
    }

    override fun retryPageLoad() {
        GlobalScope.launch(Dispatchers.IO) {
            mActivity.loadNextPage()
        }

    }


    class TopMoviesVH(binding: MovieItemBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: MovieItemBinding = binding
        fun bind(obj: Any?) {
            itemRowBinding.setVariable(BR.Search, obj)
            itemRowBinding.executePendingBindings()
        }
    }

    class LoadingVH(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root) {
        var itemRowBinding: ItemLoadingBinding = binding
    }

    fun showRetry(show: Boolean, errorMsg: String) {
        retryPageLoad = show
        notifyItemChanged(moviesModels.size - 1)
        this.errorMsg = errorMsg
    }

    fun addAll(movies: MutableList<Search>) {
        for(movie in movies){
            add(movie)
        }
    }

    fun add(moive: Search) {
        moviesModels.add(moive)
        notifyItemInserted(moviesModels.size-1)

    }

    fun addLoadingFooter() {
        isLoadingAdded = true
        add(Search())
    }

    fun removeLoadingFooter() {
        isLoadingAdded = false

        val position: Int =moviesModels.size -1
        val movie: Search = moviesModels[position]

        if(movie != null){
            moviesModels.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}