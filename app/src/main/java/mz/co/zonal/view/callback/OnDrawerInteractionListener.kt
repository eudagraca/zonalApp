package mz.co.zonal.view.callback

import mz.co.zonal.service.model.Category
import mz.co.zonal.service.model.Type


interface OnDrawerInteractionListener {
    fun sendData(category: Category? = null, type: Type? = null, min: String? = null, max: String? = null)
}