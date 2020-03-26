package mz.co.zonal.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import mz.co.zonal.service.model.Category
import mz.co.zonal.service.repository.CategoryRepository
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.utils.lazyDeferred

class CategoryViewModel(
    private val repository: CategoryRepository
) : ViewModel() {

    private lateinit var job: Job

    private val _typesList = MutableLiveData<List<Category>>()
    val categoryLiveData: LiveData<List<Category>>
        get() = _typesList


    fun fetchCategories() {
        job = Coroutine.ioThreadMain(
            { repository.fetchCategories() },
            { _typesList.value = it as List<Category> }
        )
    }

    override fun onCleared() {
        super.onCleared()
        if (::job.isInitialized) job.cancel()
    }

}