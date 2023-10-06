package com.example.unsplash.data.modelsCollections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LinksXX(
    @Json(name = "html")
    val html: String?,
    @Json(name = "photos")
    val photos: String?,
    @Json(name = "related")
    val related: String?,
    @Json(name = "self")
    val self: String?
)