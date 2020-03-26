package mz.co.zonal.service.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.repository.UserRepository
import mz.co.zonal.viewmodel.UserViewModel

class UserViewModelFactory(
    private val repository: UserRepository,
    private val session: Session

) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(repository, session) as T
    }
}