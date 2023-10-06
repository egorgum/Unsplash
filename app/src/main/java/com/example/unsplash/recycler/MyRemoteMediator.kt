package com.example.unsplash.recycler

import android.content.ContentValues.TAG
import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.example.unsplash.repository.AuthRepository
import com.example.unsplash.states.HomeState
import com.example.unsplash.room.daos.DaoHome
import com.example.unsplash.room.entities.DataBasePhotosHome
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

@ExperimentalPagingApi
class MyRemoteMediator @AssistedInject constructor(
    private val daoHome: DaoHome,
    private val repo: AuthRepository,
    @Assisted private val token: String
): RemoteMediator<Int, DataBasePhotosHome>(){

    private var pageIndex: Int = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, DataBasePhotosHome>
    ): MediatorResult {
        val pageIndex = when (loadType) {
            LoadType.REFRESH -> 0
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> ++pageIndex
        }
        val limit = state.config.pageSize
        try {
            val a = repo.getData(token = token, page = pageIndex)
            Log.d(TAG,"а: $a")
            val photos = a.map {
                DataBasePhotosHome(
                    user_name = it.user.name,
                    likes_number = it.likes,
                    avatar = it.user.profile_image.small,
                    photo_uri = it.urls.regular,
                    photo_is_liked = it.liked_by_user,
                    id_photo = it.id)
            }
            if (loadType == LoadType.REFRESH){
                daoHome.refresh(photos)
            }
            else{
                daoHome.save(photos)
            }
            HomeState.state.value = HomeState.SUCCESS
            return MediatorResult.Success(endOfPaginationReached = photos.size < limit)
        }

        catch (e:Exception){
            Log.d(TAG,"Ошибка в медиаторе: $e")
            HomeState.state.value = HomeState.Error
            return MediatorResult.Error(e)
        }
    }
    @AssistedFactory
    interface Factory {
        fun create(token: String): MyRemoteMediator
    }
}