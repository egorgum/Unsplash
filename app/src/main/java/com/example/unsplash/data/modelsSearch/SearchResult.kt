package com.example.unsplash.data.modelsSearch

import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResult(
    @Json(name = "total") val total: Int,
    @Json(name = "total_pages") val totalPages: Int,
    @Json(name = "results") val results: List<PhotosItem>
)