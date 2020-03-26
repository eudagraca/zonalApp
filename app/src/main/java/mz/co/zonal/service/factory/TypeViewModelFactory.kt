package mz.co.zonal.service.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.repository.TypeRepository
import mz.co.zonal.viewmodel.TypeViewModel

class TypeViewModelFactory (
    private val repository: TypeRepository,
    private val session: Session
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return TypeViewModel(repository, session) as T
    }
}