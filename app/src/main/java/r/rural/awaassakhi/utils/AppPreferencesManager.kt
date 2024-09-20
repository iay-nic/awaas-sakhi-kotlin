package r.rural.awaassakhi.utils

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import com.google.gson.Gson


object AppPreferencesManager {

    lateinit var preferences: SharedPreferences

    private const val PREFERENCES_FILE_NAME = "AWAAS_SAKHI_PREFERENCES"

    @JvmStatic
    fun getInstance(): AppPreferencesManager {
        return this
    }

    //********************** Use Case**********************
    // AppPreferencesManager.put(loginData, LOGIN_DATA)
    // val loginData = AppPreferencesManager.get<LoginData>(LOGIN_DATA)

    fun with(application: Application) {
        preferences = application.getSharedPreferences(
            PREFERENCES_FILE_NAME, Context.MODE_PRIVATE
        )
    }

 /*   fun clearAllPref() {
        //keep password if user choosed to save password
        val prefsToKeep = listOf(SHOULD_SAVE_PASSWORD, USER_NAME, PASSWORD)
        preferences.edit().apply {
            val allPrefs = preferences.all
            allPrefs.keys.forEach { key ->
                if (!prefsToKeep.contains(key)) {
                    remove(key)
                }
            }
            apply()
        }
    }*/
    fun clearAllPref() {
        preferences.edit().apply {
            clear()
            apply()
        }

    }
    fun <T> put(`object`: T, key: String) {
        val jsonString = Gson().toJson(`object`)
        preferences.edit().putString(key, jsonString).apply()
    }

    inline fun <reified T> get(key: String): T? {
        val value = preferences.getString(key, null)
        return if (TextUtils.isEmpty(value)) null else Gson().fromJson(value, T::class.java)
    }

    fun putString(value: String, key: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun getString(key: String): String? {
        return preferences.getString(key, null)
    }

    fun putBoolean(value: Boolean, key: String) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun getBoolean(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }


    fun putInteger(value: Int, key: String) {
        preferences.edit().putInt(key, value).apply()
    }

    fun getInteger(key: String): Int {
        return preferences.getInt(key, 0)
    }

}