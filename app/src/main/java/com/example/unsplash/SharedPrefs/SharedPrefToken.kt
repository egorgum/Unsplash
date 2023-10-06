package com.example.unsplash.SharedPrefs

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject

private const val PREF_KEY = "pref_key"
class SharedPrefToken@Inject constructor(context: Context) {

    private val prefs: SharedPreferences by lazy {
        context.getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE)
    }

    fun getText(): String? {
        return prefs.getString(PREF_KEY, null)
    }

    fun saveText(text: String) {
        prefs.edit().putString(PREF_KEY, text).apply()
    }

    fun clearText() {
        prefs.edit().clear().apply()
    }

}