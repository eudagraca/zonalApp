package mz.co.zonal.view.ui.product

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_own_products.*
import mz.co.zonal.R
import mz.co.zonal.databinding.ActivityOwnProductsBinding
import mz.co.zonal.service.model.Product
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.service.factory.ProductViewModelFactory
import mz.co.zonal.service.model.Session
import mz.co.zonal.utils.Constants
import mz.co.zonal.view.adapter.ProductItem
import mz.co.zonal.view.callback.OnRecyclerViewClickListener
import mz.co.zonal.viewmodel.ProductViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class OwnProductsActivity : AppCompatActivity(), KodeinAware, OnRecyclerViewClickListener {

    override val kodein by kodein()
    private lateinit var viewModel: ProductViewModel
    private val factory: ProductViewModelFactory by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityOwnProductsBinding = DataBindingUtil.setContentView(
            this, R.layout.activity_own_products)

        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener { onBackPressed() }
        supportActionBar!!.title = "Meus produtos"

        viewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        binding.productsVM = viewModel
        viewModel.userProducts()
        binding.lifecycleOwner = this
        bindUI()
    }

    override fun onStart() {
        super.onStart()
        shimmer_container_own_products.startShimmer()
    }

    override fun onResume() {
        super.onResume()
        shimmer_container_own_products.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmer_container_own_products.stopShimmer()
    }

    private fun bindUI() = Coroutine.main {
        viewModel.productLiveData.observe(this, Observer {
            it as List<*>
            setupRecyclerView((it as List<Product>).toProductItem())
        })
    }

    private fun List<Product>.toProductItem(): List<ProductItem> {
        return this.map {
            ProductItem(it, Session(baseContext), this@OwnProductsActivity)
        }
    }

    private fun setupRecyclerView(productItem: List<ProductItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(productItem)
        }

        mAdapter.setOnItemClickListener(onItemClick)

        rv_products_user.apply {
            layoutManager = GridLayoutManager(context!!, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }
        shimmer_container_own_products.stopShimmer()
        shimmer_container_own_products.visibility = View.GONE
        rv_products_user.visibility = View.VISIBLE
    }

    override fun onClickItemRecyclerView(view: View, productID: Long, userID: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private val onItemClick = OnItemClickListener { item, view ->
        val itemProduct = item as ProductItem
        val intent = Intent(baseContext, ProductDetailsActivity::class.java)
        val product = itemProduct.product
        intent.putExtra(Constants.PRODUCT_ID, product.id)
        startActivity(intent)
    }
}