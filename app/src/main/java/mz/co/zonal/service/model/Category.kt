package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json

@Entity
data class Category(
    @PrimaryKey(autoGenerate = false)
    @field:Json(name = "id")
    @ColumnInfo(name = "category_id")
    var id:  Long? = 0,
    @field:Json(name = "imagePath")
    var imagePath: String? = null,
    @field:Json(name = "name")
    @ColumnInfo(name = "category_name")
    var name: String? = null,
    @field:Json(name = "categoryImage")
    @ColumnInfo(name = "category_image")
    var categoryImage: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        return "$name"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(imagePath)
        parcel.writeString(name)
        parcel.writeString(categoryImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Category> {
        override fun createFromParcel(parcel: Parcel): Category {
            return Category(parcel)
        }

        override fun newArray(size: Int): Array<Category?> {
            return arrayOfNulls(size)
        }
    }
}