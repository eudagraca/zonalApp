package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import java.util.*

const val CURRENT_USER_ID: Long = 0
@Entity
data class User(
    @field:Json(name = "id")
    var id:  Long? = 0,
    @field:Json(name = "fullName")
    var fullName: String? = null,
    @field:Json(name = "email")
    var email: String? = null,
    @field:Json(name = "phoneNumber")
    var phoneNumber: Int? = null,
    @field:Json(name = "password")
    var password: String? = null,
    @field:Json(name = "createAt")
    var createAt: String? = null,
    @field:Json(name = "picPath")
    var picPath: String? = null,
    @field:Json(name = "token")
    var token: String? = null
) : Parcelable {

    @PrimaryKey(autoGenerate = false)
    var uid = CURRENT_USER_ID



    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest!!.writeLong(id!!)
        dest.writeString(fullName)
        dest.writeString(email)
        dest.writeInt(phoneNumber!!)
        dest.writeString(password)
        dest.writeValue(createAt)
        dest.writeString(picPath)
        dest.writeString(token)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }


}