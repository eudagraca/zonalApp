package mz.co.zonal.utils

import androidx.room.TypeConverter
import java.text.SimpleDateFormat
import java.util.*


class Converters {

    @TypeConverter
    fun toSimpleString(date: String) : String {
        val sdf = SimpleDateFormat(Constants.INITIAL_PATTERN_DATE)
        val output = SimpleDateFormat(Constants.FINAL_PATTERN_DATE)
        val d: Date = sdf.parse(date)!!
        return output.format(d)
    }
}