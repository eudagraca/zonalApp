package mz.co.zonal.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import mz.co.zonal.R
import mz.co.zonal.service.model.Product
import mz.co.zonal.utils.Utils
import kotlin.math.roundToInt

class ProductAdapter(
    private val products: ArrayList<Product>, val context: Context,
    private val onProductClick: (product: Product) -> Unit
) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_product, parent, false)

        return ProductViewHolder(view, context, onProductClick)
    }

    override fun getItemCount() = products.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        val product = products[position]
        holder.bindData(product)
    }


    class ProductViewHolder(
        view: View, val context: Context,
        private val onProductClick: (product: Product) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        var tvPrice: TextView? = null
        var tvName: TextView? = null
        var ivProductImage: ImageView? = null

        init {
            tvName = view.findViewById(R.id.tv_product_name)
            tvPrice = view.findViewById(R.id.tv_product_price)
            ivProductImage = view.findViewById(R.id.iv_product_image)
            ivProductImage!!.maxHeight = (Utils.getScreenHeight() / 0.04).roundToInt()
        }

        fun bindData(product: Product) {
            tvName!!.text = product.name
            tvPrice!!.text = product.price.toString()

            Glide
                .with(context)
                .load(product.image)
                .centerCrop()
                .placeholder(R.drawable.ic_picture)
                .into(ivProductImage!!)

            itemView.setOnClickListener {
                onProductClick.invoke(product)
            }
        }

    }
}