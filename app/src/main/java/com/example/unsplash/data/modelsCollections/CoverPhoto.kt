package com.example.unsplash.data.modelsCollections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CoverPhoto(
    @Json(name = "blur_hash")
    val blur_hash: String?,
    @Json(name = "color")
    val color: String?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "height")
    val height: Int?,
    @Json(name = "id")
    val id: String?,
    @Json(name = "liked_by_user")
    val liked_by_user: Boolean,
    @Json(name = "likes")
    val likes: Int,
    @Json(name = "links")
    val links: Links,
    @Json(name = "urls")
    val urls: Urls,
    @Json(name = "user")
    val user: User,
    @Json(name = "width")
    val width: Int?
)