package com.example.unsplash.recycler.PagingSources

import android.content.ContentValues
import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.data.modelsCollections.CollectionPhoto
import com.example.unsplash.repository.AuthRepository
import javax.inject.Inject

class PagingSourceCollections @Inject constructor(
    private val token: String
): PagingSource<Int, CollectionPhoto>() {
    override fun getRefreshKey(state: PagingState<Int, CollectionPhoto>): Int = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, CollectionPhoto> {
        val page = params.key?: 1
        return kotlin.runCatching {
            AuthRepository().getCollection(page = page, token = token)
        }.fold(
            onSuccess = {
                Log.d(ContentValues.TAG, "Success")
                LoadResult.Page(
                    data = it,
                    prevKey = null,
                    nextKey = if (it.isEmpty()) null else page+1
                )
            },
            onFailure = {
                Log.d(ContentValues.TAG, "Ошибка: $it")
                Throwable(it)
                LoadResult.Error(it)}
        )
    }

}