package com.example.unsplash.recycler.PagingSources

import android.content.ContentValues
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.repository.AuthRepository
import javax.inject.Inject

class PagingSourceSearch @Inject constructor(
    private val query: String,
    private val token: String
): PagingSource<Int, PhotosItem>() {
    override fun getRefreshKey(state: PagingState<Int, PhotosItem>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, PhotosItem> {
        val page = params.key?: 1
        return kotlin.runCatching {
            AuthRepository().search(page = page, query = query, token = token)
        }.fold(
            onSuccess = {
                Log.d(ContentValues.TAG, "Success")
                LoadResult.Page(
                    data = it.results,
                    prevKey = null,
                    nextKey = if (it.results.isEmpty()) null else page+1
                )
            },
            onFailure = {
                LoadResult.Error(it)}
        )
    }

}