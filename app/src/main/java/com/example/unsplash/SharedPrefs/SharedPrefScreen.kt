package com.example.unsplash.SharedPrefs

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
private const val FIRST_KEY = "FIRST_KEY"
class SharedPrefScreen@Inject constructor (context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(FIRST_KEY, Context.MODE_PRIVATE)
    }
    fun getFirst(): Boolean{
        return prefs.getBoolean(FIRST_KEY,true)
    }
    fun saveFirst(token: Boolean){
        prefs.edit().putBoolean(FIRST_KEY, token).apply()
    }
}