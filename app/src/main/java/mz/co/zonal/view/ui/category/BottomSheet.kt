package mz.co.zonal.view.ui.category

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.bottom_sheet.*
import mz.co.zonal.R
import mz.co.zonal.databinding.BottomSheetBinding
import mz.co.zonal.service.model.Category
import mz.co.zonal.utils.CategoryViewModelFactory
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.view.adapter.CategoryItem
import mz.co.zonal.viewmodel.CategoryViewModel
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance


class BottomSheet(private val click: OnClickCategory) : BottomSheetDialogFragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: CategoryViewModelFactory by instance()
    private lateinit var viewModel: CategoryViewModel
    private lateinit var dialog: BottomSheetDialog
    private lateinit var behavior: BottomSheetBehavior<View>

    companion object {
        fun newInstance(click: OnClickCategory) = BottomSheet(click).apply {

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        dialog.setOnShowListener {
            val d = it as BottomSheetDialog

            val sheet = d.findViewById<View>(
                com.google.android.material.R
                    .id.design_bottom_sheet
            )
            behavior = BottomSheetBehavior.from(sheet!!)
//            behavior.peekHeight =
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
            behavior.isHideable = false
            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        return dialog
    }

    @Nullable
    override fun onCreateView(
        @NonNull inflater: LayoutInflater, @Nullable container: ViewGroup?,
        @Nullable savedInstanceState: Bundle?
    ): View? {
        val binding: BottomSheetBinding =
            DataBindingUtil.inflate(inflater, R.layout.bottom_sheet, container, false)

        viewModel = ViewModelProvider(this, factory).get(CategoryViewModel::class.java)

        
        binding.categoryVM = viewModel
        viewModel.fetchCategories()
        binding.lifecycleOwner = this
        bindUI()
        return binding.root
    }

    private fun bindUI() = Coroutine.main {

        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {
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
            if (dialog.isShowing)
                dialog.hide()
            dismissAllowingStateLoss()
            val categoryItem = item as CategoryItem
            click.categorySelected(categoryItem.category)
        }

        rv_categories.apply {
            layoutManager = GridLayoutManager(context, 3)
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    inner class MyHandler {
        fun onClose(view: View) {
            behavior.state = BottomSheetBehavior.STATE_HIDDEN
            dialog.hide()
        }
    }


    interface OnClickCategory {
        fun categorySelected(category: Category)
    }
}