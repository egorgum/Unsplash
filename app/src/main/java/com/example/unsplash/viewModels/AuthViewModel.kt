package com.example.unsplash.viewModels

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.browser.customtabs.CustomTabsIntent
import androidx.lifecycle.ViewModel
import com.example.unsplash.repository.AuthRepository
import com.example.unsplash.repository.AuthService
import com.example.unsplash.states.AuthenticationState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import net.openid.appauth.AuthorizationService
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository,
                                        private val service: AuthService): ViewModel() {
    private val _authState = MutableStateFlow<AuthenticationState>(AuthenticationState.NotLoggedIn)
    val authState = _authState.asStateFlow()

    fun openLoginPage(context: Context):Intent{
        val customTabsIntent = CustomTabsIntent.Builder().build()
        val authRequest = service.request

        return AuthorizationService(context).getAuthorizationRequestIntent(
            authRequest,
            customTabsIntent
        )
    }

    fun changeAuthStateToError(str:String){
        _authState.value = AuthenticationState.Error(str)
    }
    fun changeAuthStateToLoggedIn(){
        _authState.value = AuthenticationState.LoggedIn
    }

    suspend fun getToken(code: String):String?{
        return try {
            Log.d(TAG, "получение токена началось")
            Log.d(TAG, "code: $code")
            val a = repository.getTokenRepo(code)
            Log.d(TAG, "токен: ${a.access_token}")
            a.access_token
        } catch (e:Exception) {
            _authState.value = AuthenticationState.Error(errorMsg = "ошибка получения токена: $e")
            null
        }
    }
}
