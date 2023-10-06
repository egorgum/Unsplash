package com.example.unsplash.repository

import android.net.Uri
import com.example.unsplash.states.AuthConfiguration
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationServiceConfiguration
import javax.inject.Inject

class AuthService @Inject constructor(){
    private val authConfig = AuthorizationServiceConfiguration(
        Uri.parse(AuthConfiguration.AUTH_ENDPOINT),
        Uri.parse(AuthConfiguration.TOKEN_ENDPOINT),
        null, Uri.parse(AuthConfiguration.END_SESSION_URI)
    )

    private val requestBuilder = AuthorizationRequest.Builder(
        authConfig,
        AuthConfiguration.ACCESS_KEY,
        AuthConfiguration.RESPONSE_TYPE,
        Uri.parse(AuthConfiguration.REDIRECT_URI))

    val request = requestBuilder
        .setScope(AuthConfiguration.SCOPE)
        .build()
}