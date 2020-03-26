package mz.co.zonal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.model.Type
import mz.co.zonal.service.repository.TypeRepository
import mz.co.zonal.utils.Coroutine

class TypeViewModel(
    private val repository: TypeRepository,
    private val session: Session
) : ViewModel() {

    private lateinit var job: Job

    private val _typesList = MutableLiveData<List<Type>>()
    val types: LiveData<List<Type>>
        get() = _typesList


    fun fetchTypes() {
        job = Coroutine.ioThreadMain(
            { repository.fetchTypes() },
            { _typesList.value = it as List<Type> }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }
}