package com.example.unsplash.data.modelsPhotoList

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PhotosItem(
    @Json(name = "blur_hash")
    val blur_hash: String,
    @Json(name = "color")
    val color: String,
    @Json(name = "created_at")
    val created_at: String,
    @Json(name = "current_user_collections")
    val current_user_collections: List<CurrentUserCollection>,
    @Json(name = "description")
    val description: String?,
    @Json(name = "height")
    val height: Int?,
    @Json(name = "id")
    val id: String,
    @Json(name = "liked_by_user")
    val liked_by_user: Boolean,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "links")
    val links: Links,
    @Json(name = "updated_at")
    val updated_at: String,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "user")
    val user: User,
    @Json(name = "width")
    val width: Int

)