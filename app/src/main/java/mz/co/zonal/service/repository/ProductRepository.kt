package mz.co.zonal.service.repository

import androidx.lifecycle.MutableLiveData
import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.db.preferences.PreferencesProvider
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.network.SafeAPIRequestNormal
import mz.co.zonal.service.network.ZonalEndpoint
import mz.co.zonal.utils.APIException
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.converters.FilePart
import okhttp3.MultipartBody
import org.threeten.bp.LocalDateTime
import retrofit2.Response

class ProductRepository(
    private val endPoint: ZonalEndpoint,
    private val dataBase: ZonalDatabase,
    private val preferencesProvider: PreferencesProvider
) : SafeAPIRequestNormal() {

    private val products = MutableLiveData<List<Product>>()

//    init {
//        products.observeForever {
//            save(it)
//        }
//    }

//    private fun save(productList: List<Product>?) {
//        Coroutine.io {
//            preferencesProvider.setLastSavedAt(LocalDateTime.now().toString())
//            productList?.let { it -> dataBase.getProductDao().save(it) }
//        }
//    }

//    suspend fun getAll(): LiveData<List<Product>> {
//        return withContext(Dispatchers.IO) {
////            fetchProducts()
//            dataBase.getProductDao().getAll()
//        }
//    }

    suspend fun fetchProducts(): List<Any>? {
        val lastSaved = preferencesProvider.getLastSavedAt()
//        if (lastSaved.isNullOrBlank() || isFetchNeeded(LocalDateTime.parse(lastSaved))) {
        return apiRequestNormal { endPoint.listProducts() }
//            products.postValue(response as List<Product>)
//        }
    }

    suspend fun fetchOwnProducts(id: Long): List<*>? {
        return apiRequestNormal { endPoint.ownProducts(id) }
    }

    private fun isFetchNeeded(saved: LocalDateTime): Boolean {
        return org.threeten.bp.temporal.ChronoUnit.MILLIS.between(saved, LocalDateTime.now()) >
                Constants.MINIMUM_INTERVAL_SAVE_DATA
    }

    suspend fun saveProduct(product: Product, multipart: List<MultipartBody.Part>): Boolean? {
        val response = endPoint.saveProduct(product = product)
        val error = response?.code()
        var message: String? = null

        if (response != null && response.isSuccessful) {
            val finalResponse =
                endPoint.saveProductImages(
                    FilePart.createPartFromString(response.body()!!.id.toString())!!,
                    multipart
                )

            if (finalResponse!!.body()!! && finalResponse.isSuccessful) {
                return finalResponse.body()
            } else {
                error.let {
                    when (it) {
                        400 -> {
                            message = "Solicitação Imprópria"
                        }
                        401 -> {
                            message = "Solicitação não autorizada"
                        }
                        403 -> {
                            message = "Você não tem permissão "
                        }
                        405 -> {
                            message = "Método não permitido"
                        }
                        404 -> {
                            message = "Solicitação não encontrada"
                        }
                        500 -> {
                            message = "Falha no servidor"
                        }
                        else -> {
                            message = "Ocorreu algum erro"
                        }
                    }
                    throw APIException(response.errorBody().toString())
                }
            }
        } else {
            error.let {
                when (it) {
                    400 -> {
                        message = "Solicitação Imprópria"
                    }
                    401 -> {
                        message = "Solicitação não autorizada"
                    }
                    403 -> {
                        message = "Você não tem permissão "
                    }
                    405 -> {
                        message = "Método não permitido"
                    }
                    404 -> {
                        message = "Solicitação não encontrada"
                    }
                    500 -> {
                        message = "Falha no servidor"
                    }
                    else -> {
                        message = "Ocorreu algum erro"
                    }
                }
                throw APIException(message!!)
            }
        }
    }

    suspend fun updateProduct(product: Product, multipart: List<MultipartBody.Part>?): Boolean? {
        val response = endPoint.updateProduct(product = product)
        val error = response?.code()
        var message: String? = null

        if (response != null && response.isSuccessful) {
            if (multipart?.isNotEmpty()!!) {
                val finalResponse =
                    endPoint.saveProductImages(
                        FilePart.createPartFromString(product.id.toString())!!,
                        multipart
                    )

                if (finalResponse!!.body()!! && finalResponse.isSuccessful) {
                    return finalResponse.body()
                } else {
                    error.let {
                        when (it) {
                            400 -> {
                                message = "Solicitação Imprópria"
                            }
                            401 -> {
                                message = "Solicitação não autorizada"
                            }
                            403 -> {
                                message = "Você não tem permissão "
                            }
                            405 -> {
                                message = "Método não permitido"
                            }
                            404 -> {
                                message = "Solicitação não encontrada"
                            }
                            500 -> {
                                message = "Falha no servidor"
                            }
                            else -> {
                                message = "Ocorreu algum erro"
                            }
                        }
                        throw APIException(response.errorBody().toString())
                    }
                }
            }
            return true
        } else {
            error.let {
                when (it) {
                    400 -> {
                        message = "Solicitação Imprópria"
                    }
                    401 -> {
                        message = "Solicitação não autorizada"
                    }
                    403 -> {
                        message = "Você não tem permissão "
                    }
                    405 -> {
                        message = "Método não permitido"
                    }
                    404 -> {
                        message = "Solicitação não encontrada"
                    }
                    500 -> {
                        message = "Falha no servidor"
                    }
                    else -> {
                        message = "Ocorreu algum erro ao tentar actualizar"
                    }
                }
                throw APIException(message!!)
            }
        }
    }


    suspend fun fetchProductById(id: Long): Product {
        return apiRequestNormal { endPoint.productById(id) }
    }

    suspend fun setViewProduct(productId: Long, userId: Long) {
        return endPoint.setProductView(productId, userId)
    }

    suspend fun likeOrDislike(productId: Long, userId: Long): Boolean? {
        val response = endPoint.likeOrDislike(productId, userId)
        return abstractResponse(response) as Boolean
    }

    suspend fun productByName(name: String): List<*>? {
        val response = endPoint.productSearchByName(name)
        return abstractResponse(response) as List<*>

    }

    /*
      Filters
     */

    suspend fun getByTitleCategoryAndType(title: String, category: Long, type: Long): List<*> {
        val response = endPoint.getByTitleCategoryAndType(title, category, type)
        return abstractResponse(response) as List<*>
    }

    suspend fun getByTitleCategoryAndTypePriceLessPriceThan(
        title: String,
        category: Long,
        type: Long,
        max: Double,
        min: Double
    ): List<*> {
        val response = endPoint.getByTitleCategoryAndTypePriceLessPriceThan(
            title, category, type,max, min
        )
        return abstractResponse(response) as List<*>
    }

    suspend fun getByTitleCategoryIdAndAndPriceLess(
        title: String,
        category: Long,
        max: Double,
        min: Double
    ): List<*> {
        val response = endPoint.getByTitleCategoryIdAndPriceLess(
            title, category, max, min
        )
        return abstractResponse(response) as List<*>
    }

    suspend fun getByTitleTypeId(title: String, type: Long): List<*> {
        val response = endPoint.getByTitleTypeId(title, type)
        return abstractResponse(response) as List<*>
    }

    suspend fun getByTitleByPriceLess(title: String, price: Double): List<*> {
        val response = endPoint.getByTitleByPriceLess(title, price)
        return abstractResponse(response) as List<*>
    }

    suspend fun getByTitleByPriceGreater(title: String, price: Double): List<*> {
        val response = endPoint.getByTitleByPriceThan(title, price)
        return abstractResponse(response) as List<*>
    }

    private fun abstractResponse(response: Response<*>): Any? {
        val error = response.code()
        var message: String? = null
        if (response.isSuccessful) {
            return response.body()
        } else {
            error.let {
                when (it) {
                    400 -> {
                        message = "Solicitação Imprópria"
                    }
                    401 -> {
                        message = "Solicitação não autorizada"
                    }
                    403 -> {
                        message = "Você não tem permissão "
                    }
                    405 -> {
                        message = "Método não permitido"
                    }
                    404 -> {
                        message = "Solicitação não encontrada"
                    }
                    500 -> {
                        message = "Falha no servidor"
                    }
                    else -> {
                        message = "Ocorreu algum erro"
                    }
                }
                throw APIException(message!!)
            }
        }
    }

    suspend fun getByType(id: Long): List<*> {
        val response = endPoint.getByType(id)
        return abstractResponse(response) as List<*>
    }

    suspend fun getByCategory(id: Long): List<*> {
        val response = endPoint.getByCategory(id)
        return abstractResponse(response) as List<*>
    }

    suspend fun getByCategoryAndType(category: Long, type: Long): List<*> {
        val response = endPoint.getByCategoryAndType(category, type)
        return abstractResponse(response) as List<*>
    }

    suspend fun fetchSoldProducts(id: Long):List<*> {
        return apiRequestNormal { endPoint.soldProducts(id) }
    }
}