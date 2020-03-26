package mz.co.zonal.service.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.repository.BrandRepository
import mz.co.zonal.service.repository.TypeRepository
import mz.co.zonal.viewmodel.BrandViewModel
import mz.co.zonal.viewmodel.TypeViewModel

class BrandViewModelFactory (
    private val repository: BrandRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return BrandViewModel(repository) as T
    }
}