package mz.co.zonal.view.ui.product

import CustomPagerAdapter
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.content_single_product.*
import mz.co.zonal.R
import mz.co.zonal.service.factory.ProductViewModelFactory
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.Session
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.loadImage
import mz.co.zonal.view.ui.maps.MapsActivity
import mz.co.zonal.view.ui.message.MessagesActivity
import mz.co.zonal.viewmodel.ProductViewModel
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class ProductDetailsActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: ProductViewModelFactory by instance()
    private lateinit var viewModel: ProductViewModel
    private val factoryUser: UserViewModelFactory by instance()
    private lateinit var viewModelUser: UserViewModel
    private val session: Session by instance()

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setSupportActionBar(toolbar_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null
        toolbar_details.setNavigationOnClickListener { onBackPressed() }

        val icon = resources.getDrawable(R.drawable.ic_arrow_to_left)
        icon.setColorFilter(resources.getColor(R.color.material_white), PorterDuff.Mode.SRC_IN);

        toolbar_details.navigationIcon = icon

        viewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)
        viewModelUser = ViewModelProvider(this, factoryUser).get(UserViewModel::class.java)
        val id = intent.getLongExtra(Constants.PRODUCT_ID, 0)
        viewModel.productById(id)
        fetchData()

        chat.setOnClickListener {
            startActivity(Intent(this, MessagesActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        shimmer_container_details.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmer_container_details.stopShimmer()
    }

    private fun fetchData() {
        viewModel.productLiveData.observe(this, Observer {
            val product = it as Product
            tv_price_detail.text = "${product.currency!!.code}  ${product.price}"
            tv_title_detail.text = "${product.title}"
            tv_views.text = "${product.viewCount}"
            tv_description_detail.text = "${product.description}"
            tv_type.text = "${product.type!!.name}"
            tv_likes.text = "${product.likesCount}"
            tv_category.text = "${product.category?.name}"

            tvOwnProduct.text = "${product.user?.fullName}"
            tv_location_details.text =
                "${product.user?.city}, ${product.user?.province}, ${product.user?.country}"

            tv_location_details.setOnClickListener {
                val intent = Intent(this,MapsActivity::class.java)
                intent.putExtra("latitude", product.user?.latitude)
                intent.putExtra("longitude", product.user?.longitude)
                intent.putExtra("name", product.user?.fullName)

                intent.putExtra("myLatitude", product.user?.latitude)
                intent.putExtra("myLongitude", product.user?.longitude)
                intent.putExtra("myName", product.user?.fullName)

                startActivity(intent)
            }

            if (session.loggedIn()) {
                viewModel.setViewProduct(product.id!!, session.id!!)
                loadImage(iv_profileImage_prod_details, viewModelUser.imageProfile(session.id!!))

                if (product.user?.id == session.id) {
                    btn_sold.visibility = View.VISIBLE
                    tv_edit_product.visibility = View.VISIBLE
                    ll_chat.visibility = View.GONE
                }

                if (tv_edit_product.isVisible){
                    tv_edit_product.setOnClickListener {
                        val intent = Intent(this, EditProductActivity::class.java)
                        intent.putExtra(Constants.PRODUCT, product.id!!)
                        startActivity(intent)
                    }
                }
            }
            coordinator_details.visibility = View.VISIBLE
            chat.visibility = View.VISIBLE
            shimmer_container_details.visibility = View.GONE
            shimmer_container_details.stopShimmer()

            view_pager.adapter =
                product.imagesByte?.let { it1 -> CustomPagerAdapter(baseContext, it1) }
        })
    }
}
