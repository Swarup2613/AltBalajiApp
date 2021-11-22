package com.example.altbalajiproject.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.altbalajiproject.model.Movie
import com.example.altbalajiproject.model.MovieInfo
import com.example.altbalajiproject.model.Search


@Database(entities = [Search::class,MovieInfo::class],version = 1)
 abstract class MovieDatabase :RoomDatabase() {

  abstract fun movieDao():MovieDAO

      companion object{
       @Volatile
       private var INSTANCE: MovieDatabase? = null

       fun getDatabase(context: Context): MovieDatabase {
          if (INSTANCE == null) {
            synchronized(this){
             INSTANCE = Room.databaseBuilder(context,
              MovieDatabase::class.java, "movieDB")
              .build()
          }
        }
         return INSTANCE!!
        }
      }
}