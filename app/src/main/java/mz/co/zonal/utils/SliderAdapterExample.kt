import android.content.Context
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import mz.co.zonal.R


class CustomPagerAdapter(val context: Context, private val resources: List<String>) :
    PagerAdapter() {
    override fun getCount() = resources.size

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView: View =
            LayoutInflater.from(context).inflate(R.layout.slider_layout, container, false)
        val ivPhoto: ImageView = itemView.findViewById(R.id.iv_product_image_list) as ImageView
        if (resources[position] != "") {


            val decodedBytes = Base64.decode(resources[position], Base64.DEFAULT)
            Glide.with(context)
                .load(decodedBytes)
                .centerCrop().placeholder(R.drawable.ic_picture)
                .into(ivPhoto)
        }
        container.addView(itemView)
        return itemView
    }

    override fun destroyItem(
        container: ViewGroup,
        position: Int,
        `object`: Any
    ) {

        container.removeView(`object` as MaterialCardView)
    }

}