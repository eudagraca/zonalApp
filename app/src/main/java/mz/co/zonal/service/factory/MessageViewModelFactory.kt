package mz.co.zonal.service.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import mz.co.zonal.service.repository.MessageRepository
import mz.co.zonal.viewmodel.MessageViewModel

class MessageViewModelFactory (
    private val repository: MessageRepository
): ViewModelProvider.NewInstanceFactory(){
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MessageViewModel(repository) as T
    }

}