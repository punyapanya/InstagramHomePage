package com.example.instagrampage.di

import com.example.instagrampage.ui.MainActivity
import com.example.instagrampage.ui.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class, LocalDataSourceModule::class, RepositoryModule::class])
interface AppComponent {

  fun inject(activity: MainActivity)
  fun inject(fragment: HomeFragment)
}