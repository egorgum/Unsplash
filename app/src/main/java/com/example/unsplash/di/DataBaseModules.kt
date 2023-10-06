package com.example.unsplash.di

import android.content.Context
import androidx.room.Room
import com.example.unsplash.room.daos.DaoHome
import com.example.unsplash.room.dataBases.DataBaseHome
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
class DataBaseModules {
    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DataBaseHome {
        return Room.databaseBuilder(
                context,
                DataBaseHome::class.java,
                DB_NAME
            )
                .build()
        }

        @Provides
        fun provideLaunchesDao(database: DataBaseHome): DaoHome {
            return database.photosDao()
        }

        private companion object {
            const val DB_NAME = "dbHome"
        }
}