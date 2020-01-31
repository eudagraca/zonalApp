package mz.co.zonal.service.network

import mz.co.zonal.service.model.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


interface ZonalEndpoint {

    @GET("users/")
    fun getUsers() : Call<List<User>>
    @POST("users/login")
    suspend fun login(@Body user: User): Response<User>
    @POST("users/{id}")
    fun userInfo(@Path("id") id: Long): Call<User>
    @POST("users/signUp")
    suspend fun signUp(@Body user: User): Response<User>
}