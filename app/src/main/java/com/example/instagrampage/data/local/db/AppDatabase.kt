package com.example.instagrampage.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.instagrampage.data.local.db.daos.ContentDao
import com.example.instagrampage.data.local.db.entities.Post
import com.example.instagrampage.data.local.db.entities.Story

@Database(
    entities = [Post::class, Story::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {

  abstract fun contentDao(): ContentDao

  companion object {
    @Volatile
    private var instance: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase =
        instance ?: synchronized(this) {
          instance ?: buildDatabase(context).also {
            instance = it
          }
        }

    private fun buildDatabase(appContext: Context) =
        Room.databaseBuilder(appContext, AppDatabase::class.java, "instagram")
            .fallbackToDestructiveMigration()
            .build()
  }
}