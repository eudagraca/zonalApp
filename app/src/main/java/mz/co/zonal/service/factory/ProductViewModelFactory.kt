package mz.co.zonal.service.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.repository.ProductRepository
import mz.co.zonal.viewmodel.ProductViewModel

class ProductViewModelFactory (
    private val repository: ProductRepository,
    private val session: Session
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(repository, session) as T
    }
}