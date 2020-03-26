package mz.co.zonal.utils.converters

import mz.co.zonal.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun toSimpleString(date: String) : String {
        val sdf = SimpleDateFormat(Constants.INITIAL_PATTERN_DATE)
        val output = SimpleDateFormat(Constants.FINAL_PATTERN_DATE)
        val d: Date = sdf.parse(date)!!
        return output.format(d)
    }
}