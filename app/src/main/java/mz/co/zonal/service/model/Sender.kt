package mz.co.zonal.service.model

data class Sender(
    val accountNonExpired: Boolean,
    val accountNonLocked: Boolean,
    val authorities: List<Any>,
    val city: String,
    val country: String,
    val createAt: String,
    val credentialsNonExpired: Boolean,
    val email: String,
    val enabled: Boolean,
    val fullName: String,
    val id: Int,
    val latitude: Double,
    val longitude: Double,
    val password: String,
    val phoneNumber: Int,
    val picPath: String,
    val province: String,
    val roles: List<Any>,
    val token: Any,
    val username: String
)