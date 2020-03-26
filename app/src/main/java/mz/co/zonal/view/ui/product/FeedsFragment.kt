package mz.co.zonal.view.ui.product


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import kotlinx.android.synthetic.main.content_main_feeds.view.*
import kotlinx.android.synthetic.main.fragment_feeds.*
import kotlinx.android.synthetic.main.list_product.view.*
import mz.co.zonal.R
import mz.co.zonal.databinding.FragmentFeedsBinding
import mz.co.zonal.service.factory.ProductViewModelFactory
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.service.model.Category
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.utils.Utils
import mz.co.zonal.view.adapter.ProductItem
import mz.co.zonal.view.callback.OnRecyclerViewClickListener
import mz.co.zonal.view.ui.ContainerScreen
import mz.co.zonal.viewmodel.ProductViewModel
import mz.co.zonal.viewmodel.UserViewModel
import org.jetbrains.anko.support.v4.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class FeedsFragment : Fragment(), KodeinAware, OnRecyclerViewClickListener {

    private var listener: OnFragmentInteractionListener? = null

    override val kodein by kodein()
    private val factory: ProductViewModelFactory by instance()
    private lateinit var viewModel: ProductViewModel
    private var mView: View? = null

    private val userFactory: UserViewModelFactory by instance()
    var userViewModel: UserViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentFeedsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_feeds, container, false)

        (activity as ContainerScreen?)?.lockDrawer()

        viewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        userViewModel = ViewModelProvider(
            this,
            userFactory
        ).get(UserViewModel::class.java)

        binding.viewModel = viewModel
        viewModel.allProducts()
        binding.lifecycleOwner = this
        bindUI()
        mView = binding.root

        binding.root.rootView.iv_view_categories.setOnClickListener {
            context.let {
                findNavController().navigate(R.id.action_feedsFragment_to_categorySearchActivity)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(this) {
            context?.let { Utils.closeApp(it) }
        }
        return mView
    }

    override fun onResume() {
        super.onResume()
        shimmer_container_feeds.startShimmer()
    }

    override fun onPause() {
        super.onPause()
        shimmer_container_feeds.stopShimmer()
    }

    private fun bindUI() = Coroutine.main {
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer { products ->

            products as List<Product>

            var productList: ArrayList<Product>
            userViewModel?.userLogged?.observe(viewLifecycleOwner, Observer {
                it as User
                productList = FilterProductUtil.filterToLocation(it, products)
                setupRecyclerView(productList.toProductItem())
            })
        })
    }

    private fun List<Product>.toProductItem(): List<ProductItem> {
        return this.map {
            ProductItem(it, Session(requireContext()), this@FeedsFragment)
        }
    }

    private fun setupRecyclerView(productItem: List<ProductItem>) {
        val mAdapter = GroupAdapter<com.xwray.groupie.ViewHolder>().apply {
            addAll(productItem)
            shimmer_container_feeds.visibility = View.GONE
            container_feed.visibility = View.VISIBLE
            shimmer_container_feeds.stopShimmer()
        }

        mAdapter.setOnItemClickListener(onItemClick)

        mView!!.rv_products_home.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private val onItemClick = OnItemClickListener { item, view ->
        val itemProduct = item as ProductItem
        val intent = Intent(context, ProductDetailsActivity::class.java)
        val product = itemProduct.product
        intent.putExtra(Constants.PRODUCT_ID, product.id)
        startActivity(intent)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener)
            listener = context
        else
            throw IllegalArgumentException("$context must implement OnFragmentInteractionListener")
    }

    private val clickListener = View.OnClickListener {
        //        val team = it.tag as Team
//        getPlayers(team.id)
        listener?.sendTeam(Category(1, "----"))
    }

    interface OnFragmentInteractionListener {
        fun sendTeam(playerResponse: Category)
    }

    override fun onClickItemRecyclerView(view: View, productID: Long, userID: Long) {
        when (view.id) {
            R.id.tv_product_heart -> {
                viewModel.productLDBoll.observe(viewLifecycleOwner, Observer {

                    if (it) {
                        view.tv_product_heart.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,
                            R.drawable.ic_like, 0
                        )
                    }else{
                        view.tv_product_heart.setCompoundDrawablesWithIntrinsicBounds(
                            0, 0,
                            R.drawable.ic_like_unlike, 0
                        )
                    }
                })

                viewModel.likeOrDislike(productID, userID)
            }
        }
    }
}
