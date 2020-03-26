package mz.co.zonal.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.repository.CategoryRepository
import mz.co.zonal.service.repository.ProductRepository
import mz.co.zonal.viewmodel.CategoryViewModel
import mz.co.zonal.viewmodel.ProductViewModel

class CategoryViewModelFactory (
    private val repository: CategoryRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CategoryViewModel(repository) as T
    }
}