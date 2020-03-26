package mz.co.zonal.service.network

import mz.co.zonal.utils.APIException
import retrofit2.Call


abstract class SafeAPIRequestNormal {
    suspend fun <T : Any> apiRequestNormal(call: suspend () -> Call<T>): T {
        val response = call.invoke()

        val responseFinal = response.execute()

        if (responseFinal.isSuccessful) {
            return responseFinal.body()!!
        } else {
            val error = responseFinal.code()
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
                throw APIException(message!!)
            }
        }
    }
}