package mz.co.zonal.view.adapter

import com.xwray.groupie.databinding.BindableItem
import mz.co.zonal.R
import mz.co.zonal.databinding.ListCategoriesBinding
import mz.co.zonal.service.model.Category

class CategoryItem(
    val category: Category
) : BindableItem<ListCategoriesBinding>(){
    override fun getLayout() = R.layout.list_categories

    override fun bind(viewBinding: ListCategoriesBinding, position: Int) {
        viewBinding.category = category

    }

}