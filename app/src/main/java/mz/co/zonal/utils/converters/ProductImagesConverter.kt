package mz.co.zonal.utils.converters

import androidx.room.TypeConverter
import java.util.*


class ProductImagesConverter {

    @TypeConverter
    fun fromArray(strings: List<String>): String? {
        var string = ""
        for (s in strings) string += "$s,"
        return string
    }

    @TypeConverter
    fun toArray(concatenatedStrings: String): List<String> {
        val myStrings = arrayListOf<String>()
        for (s in concatenatedStrings.split((",").toRegex()).dropLastWhile {
            myStrings.add(it)
        }.toTypedArray());
        return myStrings
    }
}