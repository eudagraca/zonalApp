package mz.co.zonal.service.network

import mz.co.zonal.service.model.Product


interface ProductDataListener {

    fun productList(productList: List<Product>)
}
