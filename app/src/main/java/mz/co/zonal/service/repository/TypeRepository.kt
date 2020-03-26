package mz.co.zonal.service.repository

import mz.co.zonal.service.model.Type
import mz.co.zonal.service.network.SafeAPIRequestNormal
import mz.co.zonal.service.network.ZonalEndpoint
import retrofit2.Call

class TypeRepository (private val endPoint: ZonalEndpoint): SafeAPIRequestNormal() {

    suspend fun fetchTypes(): List<*>? {
        return apiRequestNormal { endPoint.types() }
    }

}