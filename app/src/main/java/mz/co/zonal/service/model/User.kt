package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.squareup.moshi.Json

const val CURRENT_USER_ID: Long = 0

@Entity
data class User constructor(
    @field:Json(name = "id")
    @ColumnInfo(name = "id")
    var id: Long? = 0,
    @field:Json(name = "fullName")
    var fullName: String? = null,
    @field:Json(name = "email")
    var email: String? = null,
    @field:Json(name = "phoneNumber")
    var phoneNumber: Int? = null,
    @field:Json(name = "password")
    var password: String? = null,
    @field:Json(name = "createAt")
    @ColumnInfo(name = "createdDate_user")
    var createAt: String? = null,
    @field:Json(name = "picPath")
    var picPath: String? = null,
    @field:Json(name = "city")
    var city: String? = null,
    @field:Json(name = "province")
    var province: String? = null,
    @field:Json(name = "country")
    var country: String? = null,
    @field:Json(name = "token")
    var token: String? = null,
    @field:Json(name = "latitude")
    var latitude: Double? = null,
    @field:Json(name = "longitude")
    var longitude: Double? = null
) : Parcelable {
    @PrimaryKey(autoGenerate = false)
    var uid = CURRENT_USER_ID

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false
        if (fullName != other.fullName) return false
        if (email != other.email) return false
        if (phoneNumber != other.phoneNumber) return false
        if (password != other.password) return false
        if (createAt != other.createAt) return false
        if (picPath != other.picPath) return false
        if (token != other.token) return false
//        if (image != null) {
//            if (other.image == null) return false
//            if (!image!!.contentEquals(other.image!!)) return false
//        } else if (other.image != null) return false
        if (uid != other.uid) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (fullName?.hashCode() ?: 0)
        result = 31 * result + (email?.hashCode() ?: 0)
        result = 31 * result + (phoneNumber ?: 0)
        result = 31 * result + (password?.hashCode() ?: 0)
        result = 31 * result + (createAt?.hashCode() ?: 0)
        result = 31 * result + (picPath?.hashCode() ?: 0)
        result = 31 * result + (token?.hashCode() ?: 0)
//        result = 31 * result + (image?.contentHashCode() ?: 0)
        result = 31 * result + uid.hashCode()
        return result
    }

    override fun toString(): String {
        return "$fullName"
    }

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(fullName)
        writeString(email)
        writeValue(phoneNumber)
        writeString(password)
        writeString(createAt)
        writeString(picPath)
        writeString(token)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }
}