package mz.co.zonal.service.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import mz.co.zonal.service.model.CURRENT_USER_ID
import mz.co.zonal.service.model.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(user: User) : Long

    @Query("SELECT *FROM  user WHERE uid = $CURRENT_USER_ID")
    fun getUser(): LiveData<User>

//    @Query("UPDATE User SET image=:user_image WHERE uid = $CURRENT_USER_ID")
//    suspend fun setImage(user_image: ByteArray?)
}