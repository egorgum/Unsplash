package com.example.unsplash.states

sealed class AuthenticationState {
    object NotLoggedIn : AuthenticationState()
    object LoggedIn : AuthenticationState()
    class Error(val errorMsg: String) : AuthenticationState()
}