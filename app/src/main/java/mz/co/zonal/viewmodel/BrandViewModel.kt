package mz.co.zonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Brand
import mz.co.zonal.service.repository.BrandRepository
import mz.co.zonal.utils.Coroutine

class BrandViewModel(
    private val repository: BrandRepository
) : ViewModel() {

    private lateinit var job: Job

    private val _brandList = MutableLiveData<List<Brand>>()
    val brands: LiveData<List<Brand>>
        get() = _brandList


    fun fetchBrands() {
        job = Coroutine.ioThreadMain(
            { repository.fetchBrands() },
            { _brandList.value = it as List<Brand> }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}