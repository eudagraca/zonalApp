package mz.co.zonal.service.repository

import mz.co.zonal.service.db.ZonalDatabase
import mz.co.zonal.service.model.User
import mz.co.zonal.service.network.SaveAPIRequest
import mz.co.zonal.service.network.ZonalEndpoint


class UserRepository(
    private val endPoint: ZonalEndpoint,
    private val dataBase: ZonalDatabase
):  SaveAPIRequest(){

    suspend fun login(user: User): User {
        return apiRequest { endPoint.login(user = user) }
    }

    fun userInfo() = dataBase.getUserDao().getUser()

    suspend fun saveUser(user: User) = dataBase.getUserDao().update(user)

    suspend fun signUp(user: User): User{
        return apiRequest { endPoint.signUp(user) }
    }

}