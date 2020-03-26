package mz.co.zonal.view.ui.product


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.OnItemClickListener
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.fragment_search.*
import kotlinx.android.synthetic.main.fragment_search.view.*
import kotlinx.android.synthetic.main.list_product.view.*
import mz.co.zonal.R
import mz.co.zonal.service.factory.ProductViewModelFactory
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.service.model.*
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.view.adapter.ProductItem
import mz.co.zonal.view.callback.OnDrawerInteractionListener
import mz.co.zonal.view.callback.OnRecyclerViewClickListener
import mz.co.zonal.view.others.Message
import mz.co.zonal.view.ui.ContainerScreen
import mz.co.zonal.viewmodel.ProductViewModel
import mz.co.zonal.viewmodel.UserViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


/**
 * A simple [Fragment] subclass.
 */
class SearchFragment : Fragment(), KodeinAware, OnRecyclerViewClickListener,
    OnDrawerInteractionListener {

    override val kodein by kodein()
    private val factory: ProductViewModelFactory by instance()
    private lateinit var viewModel: ProductViewModel
    private val userFactory: UserViewModelFactory by instance()
    var userViewModel: UserViewModel? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        viewModel = ViewModelProvider(this, factory).get(ProductViewModel::class.java)

        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)

        view.shimmer_container_search.stopShimmer()

        view.tv_filter.setOnClickListener {
            (activity as ContainerScreen?)?.openDrawer(this)
        }

        var adapterOrder: ArrayAdapter<String>? = null
        val arrayOrder = arrayListOf<String>()
        arrayOrder.add("Melhores resultados  ")
        arrayOrder.add("Preço (Baixo ao alto)  ")
        arrayOrder.add("Preço (Alto ao baixo")
        arrayOrder.add("Data (Novo ao antigo)  ")
        adapterOrder =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, arrayOrder)
        adapterOrder.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(view.order_by_spinner, {
            setAdapter(adapterOrder)
        })

        view.tie_search.setOnEditorActionListener(object : TextView.OnEditorActionListener {
            override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    when {
                        view.tie_search.text.toString().isEmpty() -> {
                            Message.messageInfo(requireContext(), "Digite o que procucura")
                        }
                        view.tie_search.text.toString().length < 2 -> {
                            Message.messageInfo(requireContext(), "Digite no mínimo 2 caracteres")
                        }
                        else -> {
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE).also {
                                it as InputMethodManager
                                it.hideSoftInputFromWindow(requireView().windowToken, 0)
                            }
                            shimmer_container_search.startShimmer()
                            shimmer_container_search.showShimmer(true)
                            shimmer_container_search.visibility = View.VISIBLE
                            bindUI(view.tie_search.text.toString())
                        }
                    }
                    return true
                }
                return false
            }
        })
        return view
    }

    override fun onStart() {
        super.onStart()
        requireView().shimmer_container_search.stopShimmer()
    }

    override fun onResume() {
        super.onResume()
        requireView().shimmer_container_search.stopShimmer()
    }

    override fun onPause() {
        super.onPause()
        requireView().shimmer_container_search.stopShimmer()
    }

    private fun bindUI(text: String) = Coroutine.main {
        viewModel.productLiveData.observe(viewLifecycleOwner, Observer { products ->
            products as List<Product>
            preBuildRecyclerView(products)
        })
        viewModel.productByName(text)
    }

    private fun preBuildRecyclerView(products: List<Product>) {
        userViewModel?.userLogged?.observe(viewLifecycleOwner, Observer {
            it as User
            val productList = FilterProductUtil.filterToLocation(it, products)
            setupRecyclerView(productList.toProductItem())
        })
    }

    private fun List<Product>.toProductItem(): List<ProductItem> {
        return this.map {
            ProductItem(it, Session(requireContext()), this@SearchFragment)
        }
    }

    private fun setupRecyclerView(productItem: List<ProductItem>) {

        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(productItem)
            requireView().shimmer_container_search.stopShimmer()
        }
        mAdapter.setOnItemClickListener(onItemClick)

        requireView().rv_products_search.apply {
            layoutManager = GridLayoutManager(context, 2)
            setHasFixedSize(true)
            adapter = mAdapter
        }

        if (productItem.isNotEmpty()) {
            requireView().shimmer_container_search.visibility = View.GONE
            requireView().rv_products_search.visibility = View.VISIBLE

            shimmer_container_search.stopShimmer()
            shimmer_container_search.showShimmer(false)
        }else{

            shimmer_container_search.visibility = View.VISIBLE
            requireView().rv_products_search.visibility = View.GONE
            shimmer_container_search.stopShimmer()
            shimmer_container_search.showShimmer(false)
        }
    }

    private val onItemClick = OnItemClickListener { item, view ->
        val itemProduct = item as ProductItem
        val intent = Intent(context, ProductDetailsActivity::class.java)
        val product = itemProduct.product
        intent.putExtra(Constants.PRODUCT_ID, product.id)
        startActivity(intent)
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
                    } else {
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

    override fun sendData(category: Category?, type: Type?, min: String?, max: String?) {
        if (category != null && type != null && !min.isNullOrEmpty() && !max.isNullOrEmpty() && !tie_search.text.isNullOrEmpty()) {

            Log.i("IF", "todos")
            viewModel.getByTitleCategoryAndTypePriceLessPriceThan(
                title = tie_search.text.toString(),
                category = category.id!!, type = type.id!!, max = max.toString().toDouble(),
                min = min.toString().toDouble()
            )

        } else if (category != null && type != null && min.isNullOrEmpty() && max.isNullOrEmpty() && !tie_search.text.isNullOrEmpty()) {
            Log.i("IF", category.name!!)

            viewModel.getByTitleCategoryAndType(
                tie_search.text.toString(),
                category.id!!,
                type.id!!
            )
        } else if (category != null && type == null && !min.isNullOrEmpty() && !max.isNullOrEmpty() && !tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "type null")

            viewModel.getByTitleCategoryIdAndPriceLess(
                title = tie_search.text.toString(),
                category = category.id!!, max = max.toString().toDouble(),
                min = min.toString().toDouble()
            )
        } else if (category == null && type != null && min.isNullOrEmpty() && max.isNullOrEmpty() && !tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "category null | min is null | max is null")

            viewModel.getByTitleTypeId(
                title = tie_search.text.toString(), type = type.id!!
            )
        } else if (category == null && type != null && min.isNullOrEmpty() && max.isNullOrEmpty() && tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "all is null except type")

            viewModel.getByType(type.id!!)

        } else if (category != null && type == null && min.isNullOrEmpty() && max.isNullOrEmpty() && tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "all is null except category")

            viewModel.getByCategory(category.id!!)
        } else if (category == null && type == null && min.isNullOrEmpty() && !max.isNullOrEmpty() && !tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "all is null input and max")

            viewModel.getByTitleByPriceLess(tie_search.text.toString(), max.toString().toDouble())
        } else if (category == null && type == null && !min.isNullOrEmpty() && max.isNullOrEmpty() && !tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "all is null except min and input")

            viewModel.getByTitleByPriceGreater(
                tie_search.text.toString(),
                min.toString().toDouble()
            )
        } else if (category != null && type != null && min.isNullOrEmpty() && max.isNullOrEmpty() && tie_search.text.isNullOrEmpty()) {
            Log.i("IF", "all is null except category and type")
            viewModel.getByCategoryAndType(category.id!!, type.id!!)
        }

        viewModel.productLiveData.observe(this, Observer {
            it as List<Product>
            preBuildRecyclerView(it)
        })
    }
}
