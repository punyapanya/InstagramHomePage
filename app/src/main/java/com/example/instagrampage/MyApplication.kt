package com.example.instagrampage

import android.app.Application
import com.example.instagrampage.di.DaggerAppComponent
import com.example.instagrampage.di.DatabaseModule
import com.example.instagrampage.di.LocalDataSourceModule
import com.example.instagrampage.di.RepositoryModule

class MyApplication : Application() {

    lateinit var appComponent: DaggerAppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .databaseModule(DatabaseModule(applicationContext))
            .localDataSourceModule(LocalDataSourceModule())
            .repositoryModule(RepositoryModule())
            .build() as DaggerAppComponent
    }
}