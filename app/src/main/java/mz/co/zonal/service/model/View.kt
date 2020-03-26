package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.squareup.moshi.Json

data class View(
    @field:Json(name = "id")
    @ColumnInfo(name = "view_id")
    var id: Long? = 0,
    @field:Json(name = "user")
    @Ignore
    var user: User? = null,
    @field:Json(name = "product")
    @Ignore
    var product: Product? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readParcelable<User>(User::class.java.classLoader),
        source.readParcelable<Product>(Product::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeParcelable(user, 0)
        writeParcelable(product, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<View> = object : Parcelable.Creator<View> {
            override fun createFromParcel(source: Parcel): View = View(source)
            override fun newArray(size: Int): Array<View?> = arrayOfNulls(size)
        }
    }
}