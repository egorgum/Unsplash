package com.example.unsplash.repository

import android.content.ContentValues
import android.content.ContentValues.TAG
import android.util.Log
import com.example.unsplash.data.modelPublucAccount.PublicInfo
import com.example.unsplash.data.modelsAccount.AccountInfo
import com.example.unsplash.data.modelsCollections.CollectionPhoto
import com.example.unsplash.ui.auth.ResponseToken
import com.example.unsplash.ui.auth.RetrofitService
import com.example.unsplash.ui.auth.RetrofitServiceAuth
import com.example.unsplash.data.modelsDetail.DetailedPhotoInfo
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.data.modelsSearch.SearchResult
import javax.inject.Inject

class AuthRepository @Inject constructor() {

    suspend fun getTokenRepo (code:String): ResponseToken {
        return RetrofitServiceAuth.searchAuth.getTokenRest(code = code)
    }

    suspend fun getDetailedPhoto(token: String, id: String): DetailedPhotoInfo {
        val a = RetrofitService.searchApi.getDetailedPhotoApi(id = id, "Bearer $token")
        Log.d(TAG, "Детальное фото: $a")
        return a
    }

    suspend fun getData(token: String, page: Int): List<PhotosItem> {
        val a = RetrofitService.searchApi.getPhotosList(page = page, "Bearer $token")
        Log.d(TAG, "список фото: $a")
        return a
    }

    suspend fun like(id: String, token: String) {
        RetrofitService.searchApi.like(id = id, "Bearer $token")
        Log.d(TAG, "Лайк поставлен")
    }

    suspend fun unLike(id: String, token: String) {
        RetrofitService.searchApi.unlike(id = id, "Bearer $token")
        Log.d(TAG, "Лайк убран")
    }

    suspend fun search(query: String, page: Int,  perPage: Int = 10, token: String,):SearchResult{
        val a = RetrofitService.searchApi.searchPhotos(query = query, page = page, perPage = perPage, authHeader = "Bearer $token")
        Log.d(TAG,"Поиск выполнен")
        return a
    }

    suspend fun getCollection(page: Int,  perPage: Int = 10, token: String,):List<CollectionPhoto>{
        val a = RetrofitService.searchApi.searchCollections(page = page, perPage = perPage, authHeader = "Bearer $token")
        Log.d(TAG,"Показ коллекций выполнен")
        return a
    }

    suspend fun getDataCollection(perPage: Int = 10, token: String, page: Int, id: String): List<PhotosItem> {
        val a = RetrofitService.searchApi.searchCollectionsPhotos(page = page, id = id, authHeader = "Bearer $token", perPage = perPage)
        Log.d(TAG, "список фото: $a")
        return a
    }

    suspend fun getMe(token: String): AccountInfo {
        val a = RetrofitService.searchApi.searchMe(authHeader = "Bearer $token")
        Log.d(TAG, "я: $a")
        return a
    }
    suspend fun getPublic(token: String, username: String): PublicInfo {
        val a = RetrofitService.searchApi.searchPublicMe(username = username,authHeader = "Bearer $token")
        Log.d(TAG, "я: $a")
        return a
    }
    suspend fun getLiked(username: String, page: Int,  perPage: Int = 10, token: String,): List<PhotosItem>? {
        return try {
            val a = RetrofitService.searchApi.searchLiked(page = page, perPage = perPage, username = username, authHeader = "Bearer $token")
            Log.d(TAG, "список лайкнутых: $a")
            a
        } catch (e: Exception) {
            Log.d(TAG,"Ошибка репки: $e")
            null
        }
    }
}