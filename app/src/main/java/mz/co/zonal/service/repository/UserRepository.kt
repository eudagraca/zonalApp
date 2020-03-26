package mz.co.zonal.service.repository

import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.model.User
import mz.co.zonal.service.network.SafeAPIRequest
import mz.co.zonal.service.network.ZonalEndpoint
import mz.co.zonal.service.network.ZonalRestAPI
import okhttp3.MultipartBody
import okhttp3.RequestBody


class UserRepository(
    private val endPoint: ZonalEndpoint,
    private val dataBase: ZonalDatabase
) : SafeAPIRequest() {

    suspend fun login(user: User): User {
        return apiRequest { endPoint.login(user = user) }
    }

    fun userInfo() = dataBase.getUserDao().getUser()


    suspend fun saveUser(user: User) = dataBase.getUserDao().update(user)

    fun image(userId: Long) = ZonalRestAPI.serverUrl() + "users/photo/" + userId

    suspend fun signUp(user: User): User {
        return apiRequest { endPoint.signUp(user) }
    }

    suspend fun setCoordinators(user: User): User {
        return apiRequest { endPoint.setCoordinators(user) }
    }

    suspend fun setUserImage(userID: RequestBody, image: MultipartBody.Part): User {
        return apiRequest { endPoint.setImageUser(userID, image) }
    }

    suspend fun setToken(token: String?) {
//        return apiRequest { endPoint.setImageUser(userID, image) }
    }

}