package com.example.unsplash

import android.app.Application
import androidx.room.Room
import com.example.unsplash.room.dataBases.DataBaseHome
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onCreate() {
        super.onCreate()
        dbHome = Room.databaseBuilder(
            applicationContext,
            DataBaseHome::class.java,
            "dbHome"
        ).build()
    }
    companion object{
        lateinit var dbHome: DataBaseHome
    }
}

