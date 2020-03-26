package mz.co.zonal.view.adapter

import android.content.Context
import android.graphics.Color.green
import android.view.Gravity
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.xwray.groupie.databinding.BindableItem
import mz.co.zonal.R
import mz.co.zonal.databinding.ItemMessageBinding
import mz.co.zonal.service.model.Message
import org.jetbrains.anko.textColor
import org.jetbrains.anko.wrapContent

class MessagesItem(val message: Message, val context: Context) :
    BindableItem<ItemMessageBinding>() {

    private val userID: Long = 1

    override fun getLayout() = R.layout.item_message
    override fun bind(viewBinding: ItemMessageBinding, position: Int) {
        viewBinding.message = message

        setupMessageGravity(viewBinding)
    }


    private fun setupMessageGravity(viewBinding: ItemMessageBinding) {

        if (userID == message.sender.id) {
            viewBinding.rlMessage.apply {
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.END)
                this.layoutParams = lParams
            }

            viewBinding.tvReadSender.apply {
                if (message.isRead) {
                    this.text = "Lida"
                    this.textColor = ContextCompat.getColor(context, R.color.color_success)
                }else{
                    this.text = "Não lida"
                }
            }
        } else {
            viewBinding.rlMessage.apply {
                setBackgroundColor(ContextCompat.getColor(context, R.color.material_grey100))
                val lParams = FrameLayout.LayoutParams(wrapContent, wrapContent, Gravity.START)
                this.layoutParams = lParams
            }
        }
    }

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is MessagesItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? MessagesItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }


}