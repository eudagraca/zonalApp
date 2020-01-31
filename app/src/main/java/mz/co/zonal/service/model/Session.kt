package mz.co.zonal.service.model

import android.content.Context
import android.content.SharedPreferences
import mz.co.zonal.utils.Converters


class Session(val context: Context) {

    fun setLogIn(
        loggedIn: Boolean,
        fullName: String?,
        token: String?,
        email: String?,
        cellphone: Int?,
        id: Long?,
        createAt: String
    ) {
        editor.putBoolean(LOGIN, loggedIn)
        editor.putString(FULL_NAME, fullName)
        editor.putString(EMAIL, email)
        editor.putString(ID, id.toString())
        editor.putString(PHONE_NUMBER, cellphone.toString())
        editor.putString(TOKEN, token)
        editor.putString(CREATE_AT, createAt.toString())
        editor.apply()
        editor.commit()
    }

    fun loggedIn(): Boolean {
        return prefs.getBoolean("loggedInMode", false)
    }

    val fullName: String?
        get() = prefs.getString(FULL_NAME, "")

    val id: Int
        get() = prefs.getInt(ID, 0)


    val email: String?
        get() = prefs.getString(EMAIL, "")

    val phoneNumber: String?
        get() = prefs.getString(PHONE_NUMBER, "")

    val token: String?
        get() = prefs.getString(TOKEN, "")

    val createAt: String?
        get() = prefs.getString(CREATE_AT, "")

    companion object {
        private const val LOGIN = "loggedInMode"
        private const val MyPREFERENCES = "MyPrefs"
        private const val ID = "id"
        private const val FULL_NAME = "fullName"
        private const val EMAIL = "email"
        private const val PHONE_NUMBER = "phoneNUmber"
        private const val TOKEN = "token"
        private const val CREATE_AT = "createAt"
        private lateinit var prefs: SharedPreferences
        private lateinit var editor: SharedPreferences.Editor
    }

    init {
        prefs =
            context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        editor = prefs.edit()
    }
}