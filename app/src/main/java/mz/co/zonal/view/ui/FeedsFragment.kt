package mz.co.zonal.view.ui


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_feeds.view.*
import mz.co.zonal.R
import mz.co.zonal.service.model.Product
import mz.co.zonal.utils.ScrollBehavior
import mz.co.zonal.view.adapter.ProductAdapter
import mz.co.zonal.view.ui.product.ProductDetailsActivity

/**
 * A simple [Fragment] subclass.
 */
class FeedsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_feeds, container, false)


        with(view.rv_products_home, {
            this.layoutManager = GridLayoutManager(context!!, 2 )
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            this.adapter = ProductAdapter(products(), context!!) {
                Intent(context!!, ProductDetailsActivity::class.java).also {
                    startActivity(it)
                }
            }
        })


        return view
    }


    private fun products() = arrayListOf(

        Product("Laptop", 20000.0, "https://github.githubassets.com/images/modules/open_graph/github-octocat.png"),
        Product("Huawei", 15000.0, "https://github.blog/wp-content/uploads/2019/08/github-enterprise.png"),
        Product("LG", 5000.0, "https://github.blog/wp-content/uploads/2019/05/mona-heart-featured.png"),
        Product("Nexus 5", 6000.0, "https://techcrunch.com/wp-content/uploads/2019/08/GitHub-Education.png")
    )

}
