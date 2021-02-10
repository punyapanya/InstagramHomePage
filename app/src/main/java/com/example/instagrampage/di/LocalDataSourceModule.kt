package com.example.instagrampage.di

import com.example.instagrampage.data.local.db.daos.ContentDao
import com.example.instagrampage.data.local.localdatasource.LocalDataSource
import com.example.instagrampage.data.local.localdatasource.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalDataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(contentDao: ContentDao): LocalDataSource {
        return LocalDataSourceImpl(contentDao)
    }
}