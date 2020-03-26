package mz.co.zonal.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mz.co.zonal.service.db.dao.CategoryDao
//import mz.co.zonal.service.db.dao.ProductDao
import mz.co.zonal.service.db.dao.UserDao
import mz.co.zonal.service.model.Category
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.converters.Converters
import mz.co.zonal.utils.converters.ProductImagesConverter
import mz.co.zonal.utils.converters.UserConverter


@Database(
    entities = [User::class, Category::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class, UserConverter::class, ProductImagesConverter::class)


abstract class ZonalDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao
//    abstract fun getProductDao(): ProductDao
    abstract fun getCategoryDao(): CategoryDao

    companion object{
        @Volatile
        private var instance: ZonalDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK){
            instance?:buildDatabase(context).also {
                instance = it
            }
        }

        private fun buildDatabase(context: Context): ZonalDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                ZonalDatabase::class.java,
                Constants.DATABASE_NAME
            ).build()
        }
    }

}