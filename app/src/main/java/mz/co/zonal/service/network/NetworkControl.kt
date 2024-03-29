package mz.co.zonal.service.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import mz.co.zonal.utils.NoInternetException
import okhttp3.Interceptor
import okhttp3.Response

class NetworkControl(context: Context) : Interceptor{

    private val applicationContext = context.applicationContext

    override fun intercept(chain: Interceptor.Chain): Response {

        if(isOnline())
            throw NoInternetException("Sem conexão com internet")
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean{

        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE)
        as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }

    }

}