package com.example.unsplash.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.data.modelPublucAccount.PublicInfo
import com.example.unsplash.data.modelsAccount.AccountInfo
import com.example.unsplash.data.modelsPhotoList.PhotosItem
import com.example.unsplash.recycler.PagingSources.PagingSourceLikedPhotos
import com.example.unsplash.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {

    private var _me: MutableStateFlow<AccountInfo?> = MutableStateFlow(null)
    val me = _me.asStateFlow()
    private var _mePublic: MutableStateFlow<PublicInfo?> = MutableStateFlow(null)
    val mePublic = _mePublic.asStateFlow()


    suspend fun getMe(token:String){
        _me.value = repository.getMe(token)
    }
    suspend fun getPublic(token:String, username:String){
        _mePublic.value = repository.getPublic(token = token, username = username)
    }

    var pagingPhoto: Flow<PagingData<PhotosItem>>? = null

    fun paging(username: String, token: String){
        pagingPhoto = Pager(
            config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { PagingSourceLikedPhotos(username = username, token = token) }
        ).flow.cachedIn(viewModelScope)
    }


    suspend fun changeLike(isLiked:Boolean, id:String, context: Context){

        if(isLiked){
            repository.unLike(id, token = SharedPrefToken(context).getText()!!)
        }
        else{
            repository.like(id, token = SharedPrefToken(context).getText()!!)
        }

    }
}