package mz.co.zonal.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mz.co.zonal.service.model.User

class UserConverter {
    @TypeConverter
    fun toString(value: String): User = Gson().fromJson(value, object : TypeToken<User>() {}.type)

}