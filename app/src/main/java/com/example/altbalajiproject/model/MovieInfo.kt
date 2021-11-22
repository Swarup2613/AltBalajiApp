package com.example.altbalajiproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_info")
data class MovieInfo(
    @PrimaryKey(autoGenerate = true)
    val id:Int?=null,
    val Actors: String?=null,
    val Awards: String?=null,
    val BoxOffice: String?=null,
    val Country: String?=null,
    val DVD: String?=null,
    val Director: String?=null,
    val Genre: String?=null,
    val Language: String?=null,
    val Metascore: String?=null,
    val Plot: String?=null,
    val Poster: String?=null,
    val Production: String?=null,
    val Rated: String?=null,
    //val Ratings: List<Rating>?=null,
    val Released: String?=null,
    val Response: String?=null,
    val Runtime: String?=null,
    val Title: String?=null,
    val Type: String?=null,
    val Website: String?=null,
    val Writer: String?=null,
    val Year: String?=null,
    val imdbID: String?=null,
    val imdbRating: String?=null,
    val imdbVotes: String?=null
)