package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json

@Entity
data class Currency(
    @field:Json(name = "id")
    @ColumnInfo(name = "currency_id")
    var id:  Long? = 0,
    @field:Json(name = "code")
    var code: String? = null,
    @field:Json(name = "currency")
    var currency: String? = null,
    @field:Json(name = "region_country")
    var region_country: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "$code"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(code)
        parcel.writeString(currency)
        parcel.writeString(region_country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Currency> {
        override fun createFromParcel(parcel: Parcel): Currency {
            return Currency(parcel)
        }

        override fun newArray(size: Int): Array<Currency?> {
            return arrayOfNulls(size)
        }
    }
}