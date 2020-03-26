package mz.co.zonal.view.ui.product

import android.location.Location
import mz.co.zonal.service.model.Product
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.Constants

class FilterProductUtil {

    companion object {
        fun filterToLocation(user: User, products: List<Product>): ArrayList<Product> {
            val productList = arrayListOf<Product>()

            for (product in products) {
                //Current user coordinators
                val locationOne = Location("locationA")
                locationOne.latitude = user.latitude!!
                locationOne.longitude = user.longitude!!
                //Own of product coordinators
                val locationTwo = Location("locationB")
                locationTwo.latitude = product.user?.latitude!!
                locationTwo.longitude = product.user?.longitude!!

//                val distanceInMeters: Float = locationOne.distanceTo(locationTwo) / 1000
//                if (distanceInMeters < Constants.LESS_THAN_FIVE) {
                    productList.add(product)
//                }
            }
            return productList
        }
    }
}