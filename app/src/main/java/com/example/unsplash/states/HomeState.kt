package com.example.unsplash.states

import kotlinx.coroutines.flow.MutableStateFlow

sealed class HomeState {
    object SUCCESS : HomeState()
    object Error : HomeState()
    companion object{
        var state: MutableStateFlow<HomeState> = MutableStateFlow(SUCCESS)
    }
}
