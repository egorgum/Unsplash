package com.example.unsplash.room.daos

import androidx.paging.PagingSource
import androidx.room.*
import com.example.unsplash.room.entities.DataBasePhotosHome

@Dao
interface DaoHome {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(info:  List<DataBasePhotosHome>)
    @Query("SELECT * FROM photosHome")
    fun getPagingSource(): PagingSource<Int, DataBasePhotosHome>
    @Update
    suspend fun updatePhotos(info: DataBasePhotosHome)
    @Query("DELETE FROM photosHome WHERE id > 0")
    suspend fun clear()
    @Transaction
    suspend fun refresh(info: List<DataBasePhotosHome>){
        clear()
        save(info)
    }

}