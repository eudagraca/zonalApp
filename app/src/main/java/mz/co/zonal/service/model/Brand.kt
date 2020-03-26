package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json

@Entity
data class Brand(
    @field:Json(name = "id")
    @ColumnInfo(name = "brand_id")
    var id: Long? = 0,
    @field:Json(name = "name")
    @ColumnInfo(name = "brand_name")
    var name: String? = null
) : Parcelable {
    override fun toString(): String {
        return "$name"
    }

    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(name)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Brand> = object : Parcelable.Creator<Brand> {
            override fun createFromParcel(source: Parcel): Brand = Brand(source)
            override fun newArray(size: Int): Array<Brand?> = arrayOfNulls(size)
        }
    }
}