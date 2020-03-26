package mz.co.zonal.service.network

import mz.co.zonal.service.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*


interface ZonalEndpoint {

    /**
     * User
     **/
    @POST("users/login")
    suspend fun login(@Body user: User): Response<User>

    @GET("users/{id}")
    fun userInfo(@Path("id") id: Long): Call<User>

    @POST("users/signUp")
    suspend fun signUp(@Body user: User): Response<User>

    @GET("users/photo/{userId}")
    fun getUserPhoto(@Path("userId") userId: Long?): Response<String?>?

    @PUT("users/setCoordinators")
    suspend fun setCoordinators(@Body user: User): Response<User>

    @Multipart
    @PUT("users/upload")
    suspend fun setImageUser(
        @Part("userId") userId: RequestBody,
        @Part picPath: MultipartBody.Part
    ): Response<User>


    /**
     * Products
     **/
    @GET("product/")
    fun listProducts(): Call<List<Product>>

    @GET("product/user/{userId}")
    fun ownProducts(@Path("userId") userId: Long): Call<List<Product>>

    @GET("product/{productId}")
    fun productById(@Path("productId") productId: Long): Call<Product>

    @POST("product/")
    suspend fun saveProduct(
        @Body product: Product
    ): Response<Product?>?

    @Multipart
    @POST("product/images/")
    suspend fun saveProductImages(
        @Part("productId") productId: RequestBody,
        @Part files: List<MultipartBody.Part>
    ): Response<Boolean?>?

    @GET("product/search/{title}")
    suspend fun productSearchByName(@Path("title") title: String): Response<List<Product>>

    @PUT("product/")
    suspend fun updateProduct(@Body product: Product): Response<ResponseBody>?

    @GET("product/sold/user/{userId}")
    fun soldProducts(@Path("userId") userId: Long): Call<List<Product>>

    /**
     * Category
     **/
    @GET("categories/")
    fun categories(): Call<List<Category>>

    /**
     * Type
     **/
    @GET("type/")
    fun types(): Call<List<Type>>

    /**
     * Brand
     **/
    @GET("brands/")
    fun brands(): Call<List<Brand>>

    /**
     * Currency
     **/
    @GET("currency/")
    fun currencies(): Call<List<Currency>>

    /**
     * View
     **/
    @POST("views/set/{productId}/{userId}")
    suspend fun setProductView(@Path("productId") productId: Long, @Path("userId") userId: Long)

    /**
     * Likes
     * **/
    @POST("likes/likeOrDislike/{productId}/{userId}")
    suspend fun likeOrDislike(@Path("productId") productId: Long, @Path("userId") userId: Long)
            : Response<Boolean>

    /*
     * Filters
     */

    @GET("product/search/{title}/{category}/{type}")
    suspend fun getByTitleCategoryAndType(
        @Path("title") title: String
        , @Path("category") category: Long,
        @Path("type") type: Long
    ): Response<List<Product>>

    @GET("product/search/{title}/{category}/{type}/{priceLess}/{priceGreater}")
    suspend fun getByTitleCategoryAndTypePriceLessPriceThan(
        @Path("title") title: String
        , @Path("category") category: Long
        , @Path("type") type: Long
        , @Path("priceLess") max: Double
        , @Path("priceGreater") min: Double
    ): Response<List<Product>>

    @GET("product/search/{title}/category/{category}/price/{priceLess}/{priceGreater}")
    suspend fun getByTitleCategoryIdAndPriceLess(
        @Path("title") title: String
        , @Path("category") category: Long
        , @Path("priceLess") max: Double
        , @Path("priceGreater") min: Double
    ): Response<List<Product>>

    @GET("product/search/{title}/{type}")
    suspend fun getByTitleTypeId(
        @Path("title") title: String
        , @Path("type") type: Long
    ): Response<List<Product>>

    @GET("product/search/{title}/less/{price}")
    suspend fun getByTitleByPriceLess(
        @Path("title") title: String
        , @Path("price") price: Double
    ): Response<List<Product>>

    @GET("product/search/{title}/greater/{price}")
    suspend fun getByTitleByPriceThan(
        @Path("title") title: String
        , @Path("price") price: Double
    ): Response<List<Product>>

    @GET("product/search/type/{id}")
    suspend fun getByType(@Path("id") id: Long): Response<List<Product>>

    @GET("product/category/{categoryId}")
    suspend fun getByCategory(@Path("categoryId") categoryId: Long): Response<List<Product>>

    @GET("product/search/category/{categoryId}/type/{typeId}")
    suspend fun getByCategoryAndType(@Path("categoryId") category: Long, @Path("typeId") type: Long)
            : Response<List<Product>>

    @POST("messages/send")
    suspend fun sendMessage(@Body message: HashMap<String, String>): Response<Message>

    @GET("product/messages/{userId}/{productId}")
    suspend fun startMessage(@Path("userId") userId: Long, @Path("productId") productId: Long): Response<List<Message>>

    @GET("product/messages/{userId}")
    suspend fun fetchMessage(@Path("userId") userId: Long): Response<List<Product>>

}