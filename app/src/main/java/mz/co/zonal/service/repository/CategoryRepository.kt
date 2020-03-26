package mz.co.zonal.service.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.db.preferences.PreferencesProvider
import mz.co.zonal.service.model.Category
import mz.co.zonal.service.network.SafeAPIRequestNormal
import mz.co.zonal.service.network.ZonalEndpoint
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import org.threeten.bp.LocalDateTime
import retrofit2.Call

class CategoryRepository(
    private val endPoint: ZonalEndpoint,
    private val dataBase: ZonalDatabase,
    private val preferencesProvider: PreferencesProvider
) : SafeAPIRequestNormal() {

    private val categories = MutableLiveData<List<Category>>()

    init {
        categories.observeForever {
            save(it)
        }
    }

    private fun save(categoriesList: List<Category>) {
        Coroutine.io {
            preferencesProvider.setLastSavedCategoryAt(LocalDateTime.now().toString())
            categoriesList.let { dataBase.getCategoryDao().save(it) }
        }
    }

    suspend fun getAll(): LiveData<List<Category>> {
        return withContext(Dispatchers.IO) {
            fetchCategories()
            dataBase.getCategoryDao().getAll()
        }
    }

    suspend fun fetchCategories(): List<Any>? {

//        val lastSaved = preferencesProvider.getLastSavedCategoryAt()
//        if (isFetchNeeded(LocalDateTime.parse(lastSaved))) {
            return apiRequestNormal { endPoint.categories() }
//            categories.postValue(response as List<Category>)
//        }
    }

    private fun isFetchNeeded(saved: LocalDateTime): Boolean {
        return org.threeten.bp.temporal.ChronoUnit.HOURS.between(saved, LocalDateTime.now()) >
                Constants.MINIMUM_INTERVAL_SAVE_DATA
    }


}
