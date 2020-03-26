package mz.co.zonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Brand
import mz.co.zonal.service.model.Currency
import mz.co.zonal.service.repository.BrandRepository
import mz.co.zonal.service.repository.CurrencyRepository
import mz.co.zonal.utils.Coroutine

class CurrencyViewModel(
    private val repository: CurrencyRepository
) : ViewModel() {

    private lateinit var job: Job

    private val _currencyList = MutableLiveData<List<Currency>>()
    val currency: LiveData<List<Currency>>
        get() = _currencyList


    fun fetchCurrencies() {
        job = Coroutine.ioThreadMain(
            { repository.fetchCurrency() },
            { _currencyList.value = it as List<Currency> }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}