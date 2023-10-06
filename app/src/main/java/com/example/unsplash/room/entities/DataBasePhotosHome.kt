package com.example.unsplash.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "photosHome", primaryKeys = ["id"])
data class DataBasePhotosHome(
    @ColumnInfo(name = "user_name")
    val user_name: String,
    @ColumnInfo(name = "likes_number")
    val likes_number: Int,
    @ColumnInfo(name = "avatar")
    val avatar: String,
    @ColumnInfo(name = "photo_uri")
    val photo_uri: String,
    @ColumnInfo(name = "photo_is_liked")
    val photo_is_liked: Boolean,
    @ColumnInfo(name = "id")
    val id_photo: String
)