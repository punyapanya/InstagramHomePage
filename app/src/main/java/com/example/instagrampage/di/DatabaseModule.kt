package com.example.instagrampage.di

import android.content.Context
import com.example.instagrampage.data.local.db.AppDatabase
import com.example.instagrampage.data.local.db.daos.ContentDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(
    private val mAppContext: Context
) {

  @Provides
  fun provideAppContext(): Context {
    return mAppContext
  }

  @Provides
  @Singleton
  fun provideAppDatabase(appContext: Context): AppDatabase {
    return AppDatabase.getDatabase(appContext)
  }

  @Provides
  @Singleton
  fun provideContentDao(database: AppDatabase): ContentDao {
    return database.contentDao()
  }
}