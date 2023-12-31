package com.example.unsplash.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.recycler.PagingSources.PagingSourceCollectionsPhotos
import com.example.unsplash.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class ListPhotoCollectionsViewModel @Inject constructor(private val repoLike: AuthRepository): ViewModel() {
    var pagingPhoto: Flow<PagingData<PhotosItem>>? = null

    fun paging(id: String, token: String){
        pagingPhoto = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PagingSourceCollectionsPhotos(id = id, token = token) }
        ).flow.cachedIn(viewModelScope)
    }


    suspend fun changeLike(isLiked:Boolean, id:String, context: Context){

        if(isLiked){
            repoLike.unLike(id, token = SharedPrefToken(context).getText()!!)
        }
        else{
            repoLike.like(id, token = SharedPrefToken(context).getText()!!)
        }

    }
}