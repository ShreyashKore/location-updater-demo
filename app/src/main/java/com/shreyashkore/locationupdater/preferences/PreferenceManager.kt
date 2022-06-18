package com.shreyashkore.locationupdater.preferences

import android.content.Context
import androidx.core.content.edit
import com.shreyashkore.locationupdater.preferences.PreferenceManager.Keys.USER_NAME

class PreferenceManager private constructor(
    private val context: Context
) {

    private val sharedPreference = context.getSharedPreferences("user", Context.MODE_PRIVATE)

    var userName: String?
        get() = sharedPreference.getString(USER_NAME, null)
        set(value) = sharedPreference.edit { putString(USER_NAME, value) }


    object Keys {
        const val USER_NAME = "userName"
    }

    companion object {
        private var INSTANCE: PreferenceManager? = null

        fun getInstance(context: Context): PreferenceManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: PreferenceManager(context).also {
                    INSTANCE = it
                }
            }
        }
    }
}