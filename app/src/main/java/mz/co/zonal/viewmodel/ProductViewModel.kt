package mz.co.zonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.repository.ProductRepository
import mz.co.zonal.utils.Coroutine
import okhttp3.MultipartBody

class ProductViewModel(
    private val repository: ProductRepository,
    private val session: Session
) : ViewModel() {

    private lateinit var job: Job


    private var _productLiveData = MutableLiveData<Any>()
    val productLiveData: LiveData<Any>
        get() = _productLiveData

    private var _productLDBool = MutableLiveData<Boolean>()
    val productLDBoll: LiveData<Boolean>
        get() = _productLDBool

    fun allProducts() {
        job = Coroutine.ioThreadMain(
            { repository.fetchProducts() },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun userProducts() {
        if (session.loggedIn())
            job = Coroutine.ioThreadMain(
                { repository.fetchOwnProducts(session.id!!) },
                { _productLiveData.value = it as List<Product> }
            )
    }

    fun productById(id: Long) {
        job = Coroutine.ioThreadMain(
            { repository.fetchProductById(id) },
            { _productLiveData.value = it as Product }
        )
    }

    fun productByName(name: String) {
        job = Coroutine.ioThreadMain(
            { repository.productByName(name) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

    fun saveProduct(product: Product, multipart: List<MultipartBody.Part>) {
        job = Coroutine.ioThreadMain(
            { repository.saveProduct(product, multipart) },
            { _productLiveData.value = it as Boolean }
        )
    }

    fun updateProduct(product: Product, multipart: List<MultipartBody.Part>?) {
        job = Coroutine.ioThreadMain(
            { repository.updateProduct(product, multipart) },
            { _productLiveData.value = it as Boolean }
        )
    }

    fun likeOrDislike(productID: Long, userID: Long) {
        job = Coroutine.ioThreadMain(
            { repository.likeOrDislike(productID, userID) },
            { _productLDBool.value = it as Boolean }
        )
    }

    fun setViewProduct(productID: Long, userID: Long) {
        job = Coroutine.ioThreadMain(
            { repository.setViewProduct(productID, userID) },
            {}
        )
    }

    /*
        Filters
    */

    fun getByTitleCategoryAndType(title: String, category: Long, type: Long) {
        job = Coroutine.ioThreadMain(
            { repository.getByTitleCategoryAndType(title, category, type) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByTitleCategoryAndTypePriceLessPriceThan(
        title: String,
        category: Long,
        type: Long,
        max: Double,
        min: Double
    ) {
        job = Coroutine.ioThreadMain(
            { repository.getByTitleCategoryAndTypePriceLessPriceThan(title, category, type, max, min) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByTitleCategoryIdAndPriceLess(
        title: String,
        category: Long,
        max: Double,
        min: Double
    ) {
        job = Coroutine.ioThreadMain(
            { repository.getByTitleCategoryIdAndAndPriceLess(title, category, max, min) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByTitleTypeId(title: String, type: Long) {
        job = Coroutine.ioThreadMain(
            { repository.getByTitleTypeId(title, type) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByTitleByPriceLess(title: String, price: Double) {
        job = Coroutine.ioThreadMain(
            { repository.getByTitleByPriceLess(title, price) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByTitleByPriceGreater(title: String, price: Double) {
        job = Coroutine.ioThreadMain(
            { repository.getByTitleByPriceGreater(title, price) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByType(id: Long) {
        job = Coroutine.ioThreadMain(
            { repository.getByType(id) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByCategory(id: Long) {
        job = Coroutine.ioThreadMain(
            { repository.getByCategory(id) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun getByCategoryAndType(category: Long, type: Long) {
        job = Coroutine.ioThreadMain(
            { repository.getByCategoryAndType(category, type) },
            { _productLiveData.value = it as List<Product> }
        )
    }

    fun userSoldProducts() {
        if (session.loggedIn())
            job = Coroutine.ioThreadMain(
                { repository.fetchSoldProducts(session.id!!) },
                { _productLiveData.value = it as List<Product> }
            )
    }

}