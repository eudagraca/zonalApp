package mz.co.zonal.service.db.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import mz.co.zonal.utils.Constants

class PreferencesProvider (
    context: Context
){

    private val appContext = context.applicationContext
    private val preferences: SharedPreferences
    get() = PreferenceManager.getDefaultSharedPreferences(appContext)

    fun setLastSavedAt(savedAt: String){
        preferences.edit().putString(
            Constants.TIME_SAVE_AT,
            savedAt).apply()
    }

    fun getLastSavedAt(): String?{
        return preferences.getString(Constants.TIME_SAVE_CAT_AT, null)
    }
    fun setLastSavedCategoryAt(savedAt: String){
        preferences.edit().putString(
            Constants.TIME_SAVE_CAT_AT,
            savedAt).apply()
    }

    fun getLastSavedCategoryAt(): String?{
        return preferences.getString(Constants.TIME_SAVE_CAT_AT, null)
    }
}