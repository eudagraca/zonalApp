package mz.co.zonal.utils

import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import mz.co.zonal.R
import mz.co.zonal.utils.converters.DateUtils
import retrofit2.http.Url
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @JvmStatic
    fun toSimpleString(date: Date) : String {
        val format = SimpleDateFormat("dd/MM/yyy")
        return format.format(date)
    }
}

@BindingAdapter("image")
fun loadImage(imageView: ImageView, url: String) {
    Glide.with(imageView).load(url)
        .centerCrop().placeholder(R.drawable.ic_picture)
        .skipMemoryCache(true)
        .diskCacheStrategy(DiskCacheStrategy.NONE)
        .into(imageView)
}

@BindingAdapter("image_byte")
fun loadImageByte(imageView: ImageView, urlByteArray: String?) {

    if (!urlByteArray.isNullOrEmpty()) {
        val decodedBytes = Base64.decode(urlByteArray, Base64.DEFAULT)
        Glide.with(imageView)
            .load(decodedBytes)
            .centerCrop().placeholder(R.drawable.ic_picture)
            .into(imageView)

    }else{
        Glide.with(imageView).load(R.drawable.ic_picture)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("image_byte_category")
fun loadImageByteCategory(imageView: ImageView, urlByteArray: String?) {

    if (!urlByteArray.isNullOrEmpty()) {
        val decodedBytes = Base64.decode(urlByteArray, Base64.DEFAULT)
        Glide
            .with(imageView)
            .asBitmap()
            .load(decodedBytes)
            .apply(
                RequestOptions
                    .diskCacheStrategyOf(DiskCacheStrategy.ALL)
                    .dontAnimate()
                    .dontTransform()
            ).into(imageView)

    }else{
        Glide.with(imageView).load(R.drawable.ic_picture)
            .centerCrop()
            .into(imageView)
    }
}

@BindingAdapter("android:date")
fun bindServerDate(
    textView: TextView,
    date: String?
) {
    if (date !=null)
        textView.text = DateUtils.toSimpleString(date)
}