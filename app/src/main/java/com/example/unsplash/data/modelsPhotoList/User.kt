package com.example.unsplash.data.modelsPhotoList

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class User(
    @Json(name = "bio")
    val bio: String?,
    @Json(name = "id")
    val id: String,
    @Json(name = "instagram_username")
    val instagram_username: String?,
    @Json(name = "links")
    val links: LinksX,
    @Json(name = "location")
    val location: String?,
    @Json(name = "name")
    val name: String,
    @Json(name = "portfolio_url")
    val portfolio_url: String?,
    @Json(name = "profile_image")
    val profile_image: ProfileImage,
    @Json(name = "total_collections")
    val total_collections: Int,
    @Json(name = "total_likes")
    val total_likes: Int,
    @Json(name = "total_photos")
    val total_photos: Int,
    @Json(name = "twitter_username")
    val twitter_username: String?,
    @Json(name = "username")
    val username: String
)