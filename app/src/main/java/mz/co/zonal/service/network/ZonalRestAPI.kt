package mz.co.zonal.service.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class ZonalRestAPI {

    companion object {
        operator fun invoke(
            networkControl: NetworkControl
        ): ZonalEndpoint {
            val ZONAL_BASE_URL = "http://192.168.43.105:8080/rest/v01/"

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkControl)
                .build()

            return Retrofit.Builder()
                .baseUrl(ZONAL_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build().create(ZonalEndpoint::class.java)
        }
    }
}