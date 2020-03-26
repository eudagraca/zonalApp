package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import com.squareup.moshi.Json

@Entity
data class Type(
    @field:Json(name = "id")
    @ColumnInfo(name = "type_id")
    var id: Long? = 0,
    @field:Json(name = "name")
    @ColumnInfo(name = "type_name")
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
        val CREATOR: Parcelable.Creator<Type> = object : Parcelable.Creator<Type> {
            override fun createFromParcel(source: Parcel): Type = Type(source)
            override fun newArray(size: Int): Array<Type?> = arrayOfNulls(size)
        }
    }
}