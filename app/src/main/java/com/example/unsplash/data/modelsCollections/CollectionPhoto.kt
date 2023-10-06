package com.example.unsplash.data.modelsCollections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CollectionPhoto(
    @Json(name = "cover_photo")
    val cover_photo: CoverPhoto,
    @Json(name = "description")
    val description: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "last_collected_at")
    val last_collected_at: String?,
    @Json(name = "links")
    val links: LinksXX?,
    @Json(name = "private")
    val private: Boolean?,
    @Json(name = "published_at")
    val published_at: String?,
    @Json(name = "share_key")
    val share_key: String?,
    @Json(name = "title")
    val title: String,
    @Json(name = "total_photos")
    val total_photos: Int,
    @Json(name = "updated_at")
    val updated_at: String?,
    @Json(name = "user")
    val user: User
)