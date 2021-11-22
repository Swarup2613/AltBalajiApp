package com.example.altbalajiproject.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("imageFromUrl")
fun ImageView.imageFromURL(url:String)
{
    Glide.with(this.context).load(url).into(this)
}