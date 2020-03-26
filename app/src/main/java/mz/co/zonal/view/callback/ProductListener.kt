package mz.co.zonal.view.callback

import mz.co.zonal.service.model.Product

interface ProductListener {
    fun onStarted()
    fun onSuccess(product: Product)
    fun onFailure(message: String)
}