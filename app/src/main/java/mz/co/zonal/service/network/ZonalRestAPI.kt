package mz.co.zonal.service.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit


class ZonalRestAPI {

    companion object {

        val baseUrl = "http://192.168.43.215:8080/rest/v01/"

        operator fun invoke(
            networkControl: NetworkControl
        ): ZonalEndpoint {

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(networkControl)
                .build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(JacksonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .client(okHttpClient)
                .build().create(ZonalEndpoint::class.java)
        }

        fun serverUrl(): String? {
            return baseUrl
        }

    }
}