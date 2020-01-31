package mz.co.zonal.utils;

import android.content.res.Resources

object Utils {

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }
}
