package com.example.unsplash.data.modelsDetail

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Position(
    @Json(name = "latitude")
    val latitude: Double?,
    @Json(name = "longitude")
    val longitude: Double?
)