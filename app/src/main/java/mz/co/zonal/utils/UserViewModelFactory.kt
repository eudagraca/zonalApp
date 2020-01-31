package mz.co.zonal.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.repository.UserRepository
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.viewmodel.UserViewModel

class UserViewModelFactory(
                           private val repository: UserRepository

): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return UserViewModel(repository)  as T
    }
}