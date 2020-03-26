package mz.co.zonal.utils;

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Base64
import androidx.core.content.ContextCompat.startActivity
import java.io.ByteArrayOutputStream
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


object Utils {

    fun getScreenHeight(): Int {
        return Resources.getSystem().displayMetrics.heightPixels
    }

    @Throws(Exception::class)
    fun recoverImageFromUrl(urlText:String):ByteArray {
        val url = URL(urlText)
        val output = ByteArrayOutputStream()
        url.openStream().use { inputStream->
            var n: Int
            val buffer = ByteArray(1024)
            while (-1 != (inputStream.read(buffer))) {
                n= inputStream.read(buffer)
                output.write(buffer, 0, n)
            } }
        return output.toByteArray()
    }

    fun closeApp(context: Context){
        val homeIntent = Intent(Intent.ACTION_MAIN)
        homeIntent.addCategory(Intent.CATEGORY_HOME)
        homeIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        homeIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(homeIntent)
    }

    fun stringToByteArray(urlByteArray: String): ByteArray = Base64.decode(urlByteArray, Base64.DEFAULT)
}
