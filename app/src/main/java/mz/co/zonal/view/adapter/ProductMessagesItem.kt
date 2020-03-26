package mz.co.zonal.view.adapter

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.xwray.groupie.databinding.BindableItem
import mz.co.zonal.R
import mz.co.zonal.databinding.ChatItemBinding
import mz.co.zonal.databinding.ItemMessageBinding
import mz.co.zonal.databinding.ListProductBinding
import mz.co.zonal.service.model.Message
import mz.co.zonal.service.model.Product
import org.jetbrains.anko.wrapContent

class ProductMessagesItem(val userID: Long, val product: Product, val context: Context): BindableItem<ChatItemBinding>() {

    override fun getLayout() = R.layout.chat_item
    override fun bind(viewBinding: ChatItemBinding, position: Int) {
        viewBinding.chat = product

        setupMessageChat(viewBinding)
    }


    private fun setupMessageChat(viewBinding: ChatItemBinding){

        if (userID == product.user?.id){
            viewBinding.tvIsMine.apply {
                visibility = View.VISIBLE
                text = "a vender"
            }
        }else{
            viewBinding.tvIsMine.apply {
                visibility = View.VISIBLE
                text = "a comprar"
            }
        }
    }
}