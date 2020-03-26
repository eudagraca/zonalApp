package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

class ProductLikes constructor(
    @field:Json(name = "id")
    val id: Long? = 0,
    @field:Json(name = "user")
    val user: User? = null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readParcelable(User::class.java.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeParcelable(user, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ProductLikes> {
        override fun createFromParcel(parcel: Parcel): ProductLikes {
            return ProductLikes(parcel)
        }

        override fun newArray(size: Int): Array<ProductLikes?> {
            return arrayOfNulls(size)
        }
    }
}