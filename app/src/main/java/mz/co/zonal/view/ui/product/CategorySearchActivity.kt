package mz.co.zonal.view.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_category_search.*
import mz.co.zonal.R
import mz.co.zonal.databinding.ActivityCategorySearchBinding
import mz.co.zonal.service.model.Category
import mz.co.zonal.utils.CategoryViewModelFactory
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.view.adapter.CategoryItem
import mz.co.zonal.viewmodel.CategoryViewModel
import org.jetbrains.anko.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class CategorySearchActivity : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()
    private lateinit var viewModel: CategoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityCategorySearchBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_category_search)
        viewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)

        binding.tvBackFeeds.setOnClickListener {
            onBackPressed()
        }

        binding.category = viewModel
        viewModel.fetchCategories()
        binding.lifecycleOwner = this
        bindUI()
    }

    private fun bindUI() = Coroutine.main {
        viewModel.categoryLiveData.observe(this, Observer {
            setupRecyclerView(it.toCategoriesList())
        })
    }

    private fun List<Category>.toCategoriesList(): List<CategoryItem> {
        return this.map {
            CategoryItem(it)
        }
    }

    private fun setupRecyclerView(productItem: List<CategoryItem>) {
        val mAdapter = GroupAdapter<ViewHolder>().apply {
            addAll(productItem)
        }
        mAdapter.setOnItemClickListener { item, _ ->
            val categoryItem = item as CategoryItem
            toast("Info")
        }

        rv_categories_search.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

}
