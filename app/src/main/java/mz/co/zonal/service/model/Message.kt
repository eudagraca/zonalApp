package mz.co.zonal.service.model

import com.squareup.moshi.Json
import java.util.*

class Message constructor(
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "sender")
    val sender: User,
    @field:Json(name = "receiver")
    val receiver: User,
    @field:Json(name = "timeSent")
    val timeSent: Date,
    @field:Json(name = "read")
    val isRead: Boolean,
    @field:Json(name = "product")
    val product: Product
) {
    override fun toString(): String {
        return "$message"
    }
}