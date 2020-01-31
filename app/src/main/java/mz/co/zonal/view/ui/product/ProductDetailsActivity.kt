package mz.co.zonal.view.ui.product

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import kotlinx.android.synthetic.main.activity_product_details.*
import mz.co.zonal.R


class ProductDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)

        setSupportActionBar(toolbar_details)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = null
        toolbar_details.setNavigationOnClickListener {
            onBackPressed()
        }

        val imageList = ArrayList<SlideModel>()
        imageList.add(
            SlideModel(
                "https://1.bp.blogspot.com/-GUZsgr8my50/XJUWOhyHyaI/AAAAAAAABUo/bljp3LCS3SUtj-judzlntiETt7G294WcgCLcBGAs/s1600/fox.jpg",
                "Foxes live wild in the city.",
                true
            )
        )
        imageList.add(SlideModel("https://2.bp.blogspot.com/-CyLH9NnPoAo/XJUWK2UHiMI/AAAAAAAABUk/D8XMUIGhDbwEhC29dQb-7gfYb16GysaQgCLcBGAs/s1600/tiger.jpg"))
        imageList.add(
            SlideModel(
                "https://3.bp.blogspot.com/-uJtCbNrBzEc/XJUWQPOSrfI/AAAAAAAABUs/ZlReSwpfI3Ack60629Rv0N8hSrPFHb3TACLcBGAs/s1600/elephant.jpg",
                "The population of elephants is decreasing in the world."
            )
        )
        val imageSlider = findViewById<ImageSlider>(R.id.imageSlider)
        imageSlider.setImageList(imageList)


    }
}
