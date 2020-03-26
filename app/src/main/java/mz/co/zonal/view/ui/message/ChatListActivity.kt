package mz.co.zonal.view.ui.message

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_list.*
import mz.co.zonal.R
import mz.co.zonal.databinding.ActivityChatListBinding
import mz.co.zonal.service.factory.MessageViewModelFactory
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.Session
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.view.adapter.ProductMessagesItem
import mz.co.zonal.viewmodel.MessageViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ChatListActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val msFactory: MessageViewModelFactory by instance()
    private val session: Session by instance()
    lateinit var messageViewModel: MessageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_list)

        val binding: ActivityChatListBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_chat_list)

        messageViewModel = ViewModelProvider(this, msFactory).get(MessageViewModel::class.java)
        binding.viewModel = messageViewModel
        binding.lifecycleOwner = this

        session.id?.let { messageViewModel.fetchMessage(it) }

        bindUI()
    }

    private fun bindUI() = Coroutine.main {
        messageViewModel.messageLiveData.observe(this, Observer {
            it as List<Product>
            setupRecyclerView(it.toMessageItem())
        })
    }

    private fun List<Product>.toMessageItem(): List<ProductMessagesItem> {
        return this.map { product ->
            ProductMessagesItem(session.let { it.id!! }, product, this@ChatListActivity)
        }
    }

    private fun setupRecyclerView(messagesItem: List<ProductMessagesItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(messagesItem)
        }

        rv_chat_list.apply {
            layoutManager = LinearLayoutManager(context!!)
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.HORIZONTAL))
            adapter = mAdapter
        }
        mAdapter.setOnItemClickListener(onItemClick)
    }


    private val onItemClick = OnItemClickListener { item, view ->
        val itemProduct = item as ProductMessagesItem
        val intent = Intent(baseContext, MessagesActivity::class.java)
        val product = itemProduct.product
        intent.putExtra(Constants.PRODUCT_ID, product.id)
        intent.putExtra(Constants.PRODUCT_TITLE, product.title)
        intent.putExtra(Constants.PRODUCT_PRICE, product.price)
        intent.putExtra(Constants.PRODUCT_CURRENCY, product.currency?.code)
        intent.putExtra(Constants.PRODUCT_OWNER, product.user?.id)
        startActivity(intent)
    }
}