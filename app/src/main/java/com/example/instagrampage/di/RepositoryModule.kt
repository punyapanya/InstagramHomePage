package com.example.instagrampage.di

import com.example.instagrampage.data.local.localdatasource.LocalDataSource
import com.example.instagrampage.data.repository.Repository
import com.example.instagrampage.data.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

  @Provides
  @Singleton
  fun provideRepository(localDataSource: LocalDataSource): Repository {
    return RepositoryImpl(localDataSource)
  }
}