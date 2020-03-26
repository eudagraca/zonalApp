package mz.co.zonal.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mz.co.zonal.service.model.Product

//@Dao
//interface ProductDao {
//
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun save(product: List<Product>)
//
//    @Query("SELECT *FROM Product")
//    fun getAll(): LiveData<List<Product>>
//
//}