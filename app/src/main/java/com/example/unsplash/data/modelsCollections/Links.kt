package com.example.unsplash.data.modelsCollections

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Links(
    @Json(name = "download")
    val download: String,
    @Json(name = "html")
    val html: String,
    @Json(name = "self")
    val self: String
)