package mz.co.zonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Message
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.repository.MessageRepository
import mz.co.zonal.utils.Coroutine

class MessageViewModel(val repository: MessageRepository): ViewModel() {

    private lateinit var job: Job

    private val _messageLiveData = MutableLiveData<Any>()
    val messageLiveData: LiveData<Any>
        get() = _messageLiveData

    fun sendMessage(message: HashMap<String, String>){
       job = Coroutine.ioThreadMain(
           { repository.sendMessage(message) },
           { _messageLiveData.value = it as Message }
       )
    }

    fun startMessage(userId: Long, productId: Long){
        job = Coroutine.ioThreadMain(
            { repository.startMessage(userId, productId) },
            { _messageLiveData.value = it as List<Message> }
        )
    }

    fun fetchMessage(userId: Long){
        job = Coroutine.ioThreadMain(
            { repository.fetchMessages(userId) },
            { _messageLiveData.value = it as List<Product> }
        )
    }


    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}