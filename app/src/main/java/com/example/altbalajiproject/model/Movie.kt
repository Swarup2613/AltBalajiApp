package com.example.altbalajiproject.model

import androidx.room.Entity

data class Movie(
    val Response: String,
    val Search: List<Search>?,
    val totalResults: String,
    val totalPages: Int= 47


)

