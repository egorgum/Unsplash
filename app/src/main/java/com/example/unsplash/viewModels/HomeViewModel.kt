package com.example.unsplash.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import com.example.unsplash.SharedPrefs.SharedPrefToken
import com.example.unsplash.repository.AuthRepository
import com.example.unsplash.recycler.MyRemoteMediator
import com.example.unsplash.room.daos.DaoHome
import com.example.unsplash.room.entities.DataBasePhotosHome
import com.example.unsplash.states.HomeState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomeViewModel @OptIn(ExperimentalPagingApi::class)
@Inject constructor(private val daoHome: DaoHome, private val remoteMediator: MyRemoteMediator.Factory, private val repoLike: AuthRepository): ViewModel() {

    var pagingPhoto: Flow<PagingData<DataBasePhotosHome>>? = null

    suspend fun changeLike(isLiked:Boolean, id:String, context:Context){

            if(isLiked){
                repoLike.unLike(id, token = SharedPrefToken(context).getText()!!)
            }
            else{
                repoLike.like(id, token = SharedPrefToken(context).getText()!!)
            }

    }





    @OptIn(ExperimentalPagingApi::class)
    fun paging(context:Context){
        pagingPhoto = Pager(
            config = PagingConfig(pageSize = 5),
            pagingSourceFactory = { daoHome.getPagingSource() },
            remoteMediator = remoteMediator.create(SharedPrefToken(context = context).getText()!!)
        ).flow.cachedIn(viewModelScope)
        Log.d(TAG, "Значение: ${HomeState.state.value}")
    }

    fun onDelete(){
        viewModelScope.launch {
            daoHome.clear()
        }
    }




}