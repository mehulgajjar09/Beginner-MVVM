package com.mehul.corelibrary.helper

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.mehul.corelibrary.core.CoreApplication

object PreferenceHelper {
    val APP_PREF = "APP_PREF"
    fun defaultPrefs(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPrefs(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    /**
     * puts a key value pair in shared prefs if doesn't exists, otherwise updates value on given [key]
     */
    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /**
     * finds value on given key.
     * [T] is the type of value
     * @param defaultValue optional default value - will take null for strings, false for bool and -1 for numeric values if [defaultValue] is not specified
     */
    inline operator fun <reified T : Any> SharedPreferences.get(
        key: String,
        defaultValue: T? = null,
    ): T? {
        return when (T::class) {
            String::class -> getString(key, defaultValue as? String) as T?
            Int::class -> getInt(key, defaultValue as? Int ?: -1) as T?
            Boolean::class -> getBoolean(key, defaultValue as? Boolean ?: false) as T?
            Float::class -> getFloat(key, defaultValue as? Float ?: -1f) as T?
            Long::class -> getLong(key, defaultValue as? Long ?: -1) as T?
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    /*fun getUserId(): Int? {
        return getUserDetails()?.id
    }*/

    fun logout() {
        defaultPrefs(CoreApplication.getInstance()).edit().clear().apply()
    }

   /* fun setUserDetails(request: UserData) {
        defaultPrefs(CoreApplication.getInstance())["userDetails"] = Gson().toJson(request)
    }

    fun getUserDetails(): UserData? {
        val jsonString: String = defaultPrefs(CoreApplication.getInstance())["userDetails"] ?: ""
        if (jsonString.isBlank()) {
            return null
        }
        return Gson().fromJson(jsonString, UserData::class.java)
    }*/

   /* fun isUserLogin(): Boolean {
        return getUserDetails() != null
    }

    fun setForgotToken(request: String?) {
        defaultPrefs(CoreApplication.getInstance())["forgotToken"] = request
    }

    fun getForgotToken(): String? {
        return defaultPrefs(CoreApplication.getInstance())["forgotToken"]
    }

    fun setFirstTimeOpen(open: Boolean?) {
        customPrefs(CoreApplication.getInstance(), _root_ide_package_.com.mehul.corelibrary.helper.PreferenceHelper.APP_PREF)["isFirstTime"] = open
    }

    fun getFirstTimeOpen(): Boolean {
        return customPrefs(CoreApplication.getInstance(), _root_ide_package_.com.mehul.corelibrary.helper.PreferenceHelper.APP_PREF)["isFirstTime"] ?: false
    }*/
}