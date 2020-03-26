package mz.co.zonal.service.model

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class Session(val context: Context) {

    private val preferences: SharedPreferences
        get() = PreferenceManager.getDefaultSharedPreferences(context)

    fun setLogIn(
        loggedIn: Boolean,
        id: Long?) {
        editor.putBoolean(LOGIN, loggedIn)
        editor.putLong(ID, id!!)
        editor.apply()
        editor.commit()
    }

    fun setLatLong(
        lat: String,
        long: String ) {
        editor.putString(LAT, lat)
        editor.putString(LONG, long)
        editor.apply()
        editor.commit()
    }

    fun setToken(
        token: String ) {
        editor.putString(TOKEN, token)
        editor.apply()
        editor.commit()
    }

    fun setLogIn(
        loggedIn: Boolean,
        fullName: String?,
        token: String?,
        email: String?,
        cellphone: Int?,
        id: Long?,
        createAt: String?) {
        editor.putBoolean(LOGIN, loggedIn)
        editor.putString(FULL_NAME, fullName)
        editor.putString(EMAIL, email)
        editor.putLong(ID, id!!)
        editor.putString(PHONE_NUMBER, cellphone.toString())
        editor.putString(TOKEN, token)
        editor.putString(CREATE_AT, createAt.toString())
        editor.apply()
        editor.commit()
    }

    fun loggedIn(): Boolean {
        return preferences.getBoolean("loggedInMode", false)
    }

    val fullName: String?
        get() = preferences.getString(FULL_NAME, "")

    val id: Long?
        get() = preferences.getLong(ID, 0L)

    val email: String?
        get() = preferences.getString(EMAIL, "")

    val phoneNumber: String?
        get() = preferences.getString(PHONE_NUMBER, "")

    val token: String?
        get() = preferences.getString(TOKEN, "")

    val lat: String?
        get() = preferences.getString(LAT, "")

    val long: String?
        get() = preferences.getString(LONG, "")


    val createAt: String?
        get() = preferences.getString(CREATE_AT, "")

    companion object {
        private const val LOGIN = "loggedInMode"
        private const val LAT = "longitude"
        private const val LONG = "latitude"
        private const val ID = "id"
        private const val FULL_NAME = "fullName"
        private const val EMAIL = "email"
        private const val PHONE_NUMBER = "phoneNUmber"
        private const val TOKEN = "token"
        private const val CREATE_AT = "createAt"
        private lateinit var editor: SharedPreferences.Editor
    }

    init {
        editor = preferences.edit()
    }
}