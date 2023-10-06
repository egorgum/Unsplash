package com.example.unsplash.data.modelPublucAccount

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PublicInfo(
    @Json(name = "profile_image")
    val profile_image: ProfileImage
)
