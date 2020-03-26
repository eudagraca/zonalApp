package mz.co.zonal.view.adapter

import android.util.Log
import com.xwray.groupie.databinding.BindableItem
import kotlinx.android.synthetic.main.list_product.view.*
import mz.co.zonal.R
import mz.co.zonal.databinding.ListProductBinding
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.Session
import mz.co.zonal.view.callback.OnRecyclerViewClickListener


class ProductItem(
    val product: Product,
    val session: Session,
    private val onClickListener: OnRecyclerViewClickListener
) : BindableItem<ListProductBinding>() {


    override fun getLayout() = R.layout.list_product

    override fun bind(viewBinding: ListProductBinding, position: Int) {
        viewBinding.product = product

        if (session.loggedIn() and (product.productLikes != null)) {
            for (p in product.productLikes!!) {
                val isLiked = p.user!!.id == session.id
                if (isLiked){
                    Log.i("TAG", p.user?.id.toString() +"  "+p.user?.fullName)
                    viewBinding.root.tv_product_heart.setCompoundDrawablesWithIntrinsicBounds(
                        0, 0, R.drawable.ic_like, 0)
                }
            }
        }

        if (session.loggedIn() && (product.user?.id != session.id)) {
            viewBinding.tvProductHeart.setOnClickListener {
                onClickListener.onClickItemRecyclerView(
                    viewBinding.tvProductHeart,
                    product.id!!,
                    session.id!!
                )
            }
        }
    }
}