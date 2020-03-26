package mz.co.zonal.service.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.squareup.moshi.Json
import mz.co.zonal.utils.converters.Converters

//@Entity
open class Product constructor(
    @PrimaryKey(autoGenerate = true)
    @field:Json(name = "id")
    @ColumnInfo(name = "product_id")
    var id: Long? = null,
    @field:Json(name = "title")
    var title: String? = null,
    @field:Json(name = "description")
    var description: String? = null,
    @field:Json(name = "price")
    var price: Double? = 0.0,
    @field:Json(name = "category")
    @Embedded
    var category: Category? = null,
    @field:Json(name = "isSold")
    var isSold: Boolean? = false,
    @field:Json(name = "currency")
    @Embedded
    var currency: Currency? = null,
    @field:Json(name = "user")
    @Embedded
    var user: User? = null,
    @TypeConverters(Converters::class)
    @field:Json(name = "productLikes")
    var productLikes: List<ProductLikes>? = null,
    @field:Json(name = "images")
    @TypeConverters(Converters::class)
    var images: ArrayList<Images>? = null,
    @field:Json(name = "createAt")
    @ColumnInfo(name = "createdDate_product")
    var createAt: String? = null,
    @field:Json(name = "type")
    @Embedded
    var type: Type? = null,
    @field:Json(name = "likesCount")
    var likesCount: Long? = 0,
    @field:Json(name = "viewCount")
    var viewCount: Long? = 0,
    @field:Json(name = "brand")
    @Embedded
    var brand: Brand? = null,
    @field:Json(name = "imagesByte")
    var imagesByte: List<String>? = null
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readString(),
        source.readString(),
        source.readValue(Double::class.java.classLoader) as Double?,
        source.readParcelable<Category>(Category::class.java.classLoader),
        source.readValue(Boolean::class.java.classLoader) as Boolean?,
        source.readParcelable<Currency>(Currency::class.java.classLoader),
        source.readParcelable<User>(User::class.java.classLoader),
        ArrayList<ProductLikes>().apply { source.readList(this as List<*>, ProductLikes::class.java.classLoader) },
        ArrayList<Images>().apply { source.readList(this as List<*>, Images::class.java.classLoader) },
        source.readString(), source.readParcelable<Type>(Type::class.java.classLoader),
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readValue(Long::class.java.classLoader) as Long?,
        source.readParcelable<Brand>(Brand::class.java.classLoader),
        source.createStringArrayList()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(id)
        writeString(title)
        writeString(description)
        writeValue(price)
        writeParcelable(category, 0)
        writeValue(isSold)
        writeParcelable(currency, 0)
        writeParcelable(user, 0)
        writeList(productLikes)
        writeList(images as List<Images>?)
        writeString(createAt)
        writeParcelable(type,0)
        writeValue(likesCount)
        writeValue(viewCount)
        writeParcelable(brand,0)
        writeStringList(imagesByte)
    }

    override fun toString(): String {
        return "$title"
    }


    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Product> = object : Parcelable.Creator<Product> {
            override fun createFromParcel(source: Parcel): Product = Product(source)
            override fun newArray(size: Int): Array<Product?> = arrayOfNulls(size)
        }
    }
}