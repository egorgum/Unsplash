package com.example.unsplash.room.dataBases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.unsplash.room.daos.DaoHome
import com.example.unsplash.room.entities.DataBasePhotosHome

@Database(entities = [DataBasePhotosHome::class], version = 1)
abstract class DataBaseHome: RoomDatabase() {
    abstract fun photosDao(): DaoHome
}