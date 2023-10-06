package com.example.unsplash.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplash.data.modelsCollections.CollectionPhoto
import com.example.unsplash.recycler.PagingSources.PagingSourceCollections
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
@HiltViewModel
class CollectionsViewModel @Inject constructor(): ViewModel() {

    var pagingCollections: Flow<PagingData<CollectionPhoto>>? = null

    fun paging(token: String){
        pagingCollections = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PagingSourceCollections(token = token) }
        ).flow.cachedIn(viewModelScope)
    }
}