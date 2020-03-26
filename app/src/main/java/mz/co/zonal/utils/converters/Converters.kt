package mz.co.zonal.utils.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import mz.co.zonal.service.model.Images
import mz.co.zonal.service.model.ProductLikes
import java.lang.reflect.Type


object Converters {

    @TypeConverter
    fun fromString(value: String?): List<Any?>? {
        val listType: Type =
            object : TypeToken<List<Any?>?>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<Any?>?): String? {
        val gson = Gson()
        return gson.toJson(list)
    }
}