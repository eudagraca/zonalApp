package mz.co.zonal.service.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import mz.co.zonal.service.db.dao.UserDao
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Converters


@Database(
    entities = [User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)


abstract class ZonalDatabase: RoomDatabase() {

    abstract fun getUserDao(): UserDao

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