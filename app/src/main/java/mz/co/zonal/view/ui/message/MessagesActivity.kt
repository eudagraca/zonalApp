package mz.co.zonal.view.ui.message

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_messages.*
import mz.co.zonal.R
import mz.co.zonal.service.factory.MessageViewModelFactory
import mz.co.zonal.service.model.Message
import mz.co.zonal.service.model.Session
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.view.adapter.MessagesItem
import mz.co.zonal.viewmodel.MessageViewModel
import org.jetbrains.anko.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class MessagesActivity : AppCompatActivity(), KodeinAware, TextWatcher {

    override val kodein by kodein()
    private val msFactory: MessageViewModelFactory by instance()
    private val session: Session by instance()
    private lateinit var messageViewModel: MessageViewModel

    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        messageViewModel = ViewModelProvider(this, msFactory).get(MessageViewModel::class.java)
        val productID = intent.getLongExtra(Constants.PRODUCT_ID, 0)
        val productTitle = intent.getStringExtra(Constants.PRODUCT_TITLE)
        val productPrice = intent.getDoubleExtra(Constants.PRODUCT_PRICE, 0.0)
        val productIDOwner = intent.getLongExtra(Constants.PRODUCT_OWNER, 0)
        val productCurrency = intent.getStringExtra(Constants.PRODUCT_CURRENCY)
        session.id?.let { messageViewModel.startMessage(it, productID) }
        tv_title_product.text = productTitle
        tv_price_toolbar.text = "$productPrice $productCurrency"


        mT_toolbar_messager.setOnClickListener {
            onBackPressed()
        }

        isInvalid()

        iv_sender.setOnClickListener {
            if (!isInvalid()){
//                messageViewModel.messageLiveData.observe(this, Observer {
//
//                })
//
//                val message = HashMap<String, String>()
//                message["message"] = tie_message.text.toString()
//                message["product"] = productID.toString()
//
//                if (session.id == productIDOwner){
//                    message["receiver"] = tie_message.text.toString()
//                }
//
//
//                message["sender"] = session.id.toString()
//                messageViewModel.sendMessage(message)

                var message = messagesSection.getItem(0) as Message
            }
        }

        bindUI()

    }

    private fun bindUI() = Coroutine.main {
        messageViewModel.messageLiveData.observe(this, androidx.lifecycle.Observer {
            it as List<Message>
            updateRecyclerView(it.toMessageItem())
        })
    }


    private fun List<Message>.toMessageItem(): List<MessagesItem> {
        return this.map {
            MessagesItem(it, this@MessagesActivity)
        }
    }

    private fun updateRecyclerView(messages: List<MessagesItem>) {
        fun init() {
            rv_messages.apply {
                layoutManager = LinearLayoutManager(this@MessagesActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        rv_messages.scrollToPosition(rv_messages.adapter?.itemCount!! - 1)
    }

    override fun afterTextChanged(s: Editable?) {
        iv_sender.isEnabled = !isInvalid()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        when {
            tie_message.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() || s.isEmpty() -> {
                        tie_message.error = "\nInsira o nome"
                        return
                    }
                    s.length < 2 -> {
                        tie_message.error = "\nMínimo 2 caracteres"
                        return
                    }
                }
            }
            else -> {
                tie_message.error = null
            }
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when {
            tie_message.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() || s.isEmpty() -> {
                        tie_message.error = "\nInsira o nome"
                        return
                    }
                    s.length < 2 -> {
                        tie_message.error = "\nMínimo 2 caracteres"
                        return
                    }
                }
            }
            else -> {
                tie_message.error = null
            }
        }
    }

    private fun isInvalid() =
        (tie_message.text.toString().isEmpty() || tie_message.text.toString().length < 3)

}
