package mz.co.zonal.service.repository

import mz.co.zonal.service.model.Message
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.network.SafeAPIRequest
import mz.co.zonal.service.network.ZonalEndpoint

class MessageRepository(val db: ZonalEndpoint) : SafeAPIRequest() {

    suspend fun sendMessage(message: HashMap<String, String>): Message {
        return apiRequest { db.sendMessage(message) }
    }

    suspend fun startMessage(userId: Long, productId: Long): List<Message> {
        return apiRequest { db.startMessage(userId, productId) }
    }


    suspend fun fetchMessages(userId: Long): List<Product> {
        return apiRequest { db.fetchMessage(userId) }
    }

}