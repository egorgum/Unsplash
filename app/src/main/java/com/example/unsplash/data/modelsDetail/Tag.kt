package com.example.unsplash.data.modelsDetail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Tag(
    @Json(name = "title")
    val title: String?
)