package mz.co.zonal.service.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.repository.BrandRepository
import mz.co.zonal.service.repository.CurrencyRepository
import mz.co.zonal.service.repository.TypeRepository
import mz.co.zonal.viewmodel.BrandViewModel
import mz.co.zonal.viewmodel.CurrencyViewModel
import mz.co.zonal.viewmodel.TypeViewModel

class CurrencyViewModelFactory (
    private val repository: CurrencyRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrencyViewModel(repository) as T
    }
}