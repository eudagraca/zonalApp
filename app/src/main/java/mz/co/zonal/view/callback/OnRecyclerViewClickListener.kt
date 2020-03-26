package mz.co.zonal.view.callback

import android.view.View

interface OnRecyclerViewClickListener {
    fun onClickItemRecyclerView(view: View, productID: Long, userID: Long)
}