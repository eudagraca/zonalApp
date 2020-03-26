package mz.co.zonal.service.repository

import mz.co.zonal.service.model.Brand
import mz.co.zonal.service.model.Currency
import mz.co.zonal.service.model.Type
import mz.co.zonal.service.network.SafeAPIRequestNormal
import mz.co.zonal.service.network.ZonalEndpoint
import retrofit2.Call

class CurrencyRepository (private val endPoint: ZonalEndpoint): SafeAPIRequestNormal() {

    suspend fun fetchCurrency(): List<*>? {
        return apiRequestNormal { endPoint.currencies() }
    }

}