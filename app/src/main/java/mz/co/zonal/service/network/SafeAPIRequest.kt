package mz.co.zonal.service.network

import mz.co.zonal.utils.APIException
import retrofit2.Response

abstract class SafeAPIRequest {
    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()

        if (response.isSuccessful) {
            return response.body()!!
        } else {
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
                    500 -> {
                        message = "Falha no servidor"
                    }
                    else -> {
                        message = "Ocorreu algum erro"
                    }
                }
                throw APIException(response.message()!!)
            }
        }
    }
}