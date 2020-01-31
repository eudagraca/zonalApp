package mz.co.zonal.view.ui.product

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.activity_own_products.*
import mz.co.zonal.R
import mz.co.zonal.service.model.Product
import mz.co.zonal.view.adapter.ProductAdapter

class OwnProductsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_own_products)


        setSupportActionBar(toolBar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }

        with(rv_products_user, {
            this.layoutManager = GridLayoutManager(context!!, 2)
            setHasFixedSize(true)
            isNestedScrollingEnabled = false
            this.adapter = ProductAdapter(products(), context!!){
                Intent(context!!, ProductDetailsActivity::class.java).also {
                    startActivity(it)
                }
            }
        })
    }

    private fun products() = arrayListOf(

        Product("Laptop", 20000.0, "https://github.com/bumptech/glide/blob/master/static/glide_logo.png?raw=true"),
        Product("Huawei", 15000.0, "https://github.blog/wp-content/uploads/2019/08/github-enterprise.png"),
        Product("LG", 5000.0, "https://github.blog/wp-content/uploads/2019/05/mona-heart-featured.png"),
        Product("Nexus 5", 6000.0, "https://techcrunch.com/wp-content/uploads/2019/08/GitHub-Education.png")
    )
}
