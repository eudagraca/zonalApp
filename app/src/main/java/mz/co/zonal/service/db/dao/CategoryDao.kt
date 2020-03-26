package mz.co.zonal.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mz.co.zonal.service.model.Category

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(category: List<Category>)

    @Query("SELECT *FROM Category")
    fun getAll(): LiveData<List<Category>>
}