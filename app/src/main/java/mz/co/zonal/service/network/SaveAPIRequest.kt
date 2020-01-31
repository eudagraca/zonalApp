package mz.co.zonal.service.network

import android.util.Log
import mz.co.zonal.utils.APIException
import retrofit2.HttpException
import retrofit2.Response

abstract class SaveAPIRequest {
    suspend fun <T: Any> apiRequest(call: suspend () -> Response<T>): T{
        val response = call.invoke()

        if (response.isSuccessful){
            return response.body()!!
        }else{
            val error = response.code()
            var message: String? = null

            error.let {
                when (it) {
                    400 -> {
                        message = "Solicitação Imprópria"
                    }
                    401 -> {
                        message = "Solicitação não autorizada"
                    }
                    403 -> {
                        message = "Você não tem permissão "
                    }
                    405 -> {
                        message = "Método não permitido"
                    }
                    404 -> {
                        message = "Solicitação não encontrada"
                    }
                }
//                Log.i("INFOR", response.headers().toString())
//                Log.i("BODY",  response.raw().networkResponse().toString())
                throw APIException( message!!)
            }
        }
    }
}