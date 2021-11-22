package com.example.altbalajiproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class Search(

    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val Poster: String?=null,
    val Title: String?=null,
    val Type: String?=null,
    val Year: String?=null,
    val imdbID: String?=null

)
