package mz.co.zonal.view.ui.product

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import com.tiper.MaterialSpinner
import kotlinx.android.synthetic.main.form.*
import mz.co.zonal.R


class SellProductActivity : AppCompatActivity() {


    private val listener by lazy {
        object : MaterialSpinner.OnItemSelectedListener {
            override fun onItemSelected(parent: MaterialSpinner, view: View?, position: Int, id: Long) {
                type_spinner.editText!!.text = parent.editText!!.text
                Log.v("MaterialSpinner", "onItemSelected parent=${parent.editText!!.text}, position=$position")
                parent.focusSearch(View.FOCUS_UP)?.requestFocus()
            }

            override fun onNothingSelected(parent: MaterialSpinner) {
                Log.v("MaterialSpinner", "onNothingSelected parent=${parent.id}")
            }
        }
    }

    var imagesEncodedList: ArrayList<Uri>? = arrayListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_product)

        openGallery!!.setOnClickListener {

            selectImage()

        }

        ArrayAdapter.createFromResource(this, R.array.planets_array, android.R.layout.simple_spinner_item).let {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            type_spinner.apply {
                adapter = it
                onItemSelectedListener = listener
                onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
                    Log.v("MaterialSpinner", "onFocusChange hasFocus=$hasFocus")
                }
            }
        }

    }

    private fun selectImage() {
        if (ContextCompat.checkSelfPermission(this,
                WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    1)
            }

            return
        }


        val kwikPicker = KwikPicker.Builder(this@SellProductActivity,
            imageProvider = { imageView, uri ->
                Glide.with(this@SellProductActivity)
                    .load(uri)
                    .into(imageView)
            },
            onMultiImageSelectedListener = { lista: ArrayList<Uri> ->

                var list = lista

                if (list.isNotEmpty() && list.size == 1)
                    if (list[0].toString().isNotEmpty()){
//                        imgOne.setImageURI(list[0])
                        Glide.with(this)
                            .load(list[0])
                            .centerCrop()
                            .placeholder(R.drawable.ic_camera_icon)
                            .into(imgOne)
//                        imagesEncodedList!!.add(0, list[0])
                    }

                if (list.isNotEmpty() && list.size == 2)
                    if (list[1].toString().isNotEmpty()){
//                        imgTwo.setImageURI(list[1])
                        Glide.with(this)
                            .load(list[1])
                            .centerCrop()
                            .placeholder(R.drawable.ic_camera_icon)
                            .into(imgTwo)
//                        imagesEncodedList!!.add(1, list[1])
                    }

                if (list.isNotEmpty() && list.size == 3)
                    if (list[2].toString().isNotEmpty()){
                        Glide.with(this)
                            .load(list[2])
                            .centerCrop()
                            .placeholder(R.drawable.ic_camera_icon)
                            .into(imgThree)
//                        imgThree.setImageURI(list[2])
//                        imagesEncodedList!!.add(2, list[2])
                    }

                if (list.isNotEmpty() && list.size == 4)
                    if (list[3].toString().isNotEmpty()){
//                        imgFour.setImageURI(list[3])
                        biuldImage(imgThree, list[3])
//                        imagesEncodedList!!.add(3, list[3])
                    }

                if (list.isNotEmpty() && list.size == 5)
                    if (list[4].toString().isNotEmpty()){
                        imgFive.setImageURI(list[4])
//                        imagesEncodedList!!.add(4, list[4])
                    }

                if (list.isNotEmpty() && list.size == 6)
                    if (list[5].toString().isNotEmpty()){
                        imgSix.setImageURI(list[5])
//                        imagesEncodedList!!.add(5, list[5])
                    }
                if (list.isNotEmpty() && list.size == 7)
                    if (list[6].toString().isNotEmpty()){
                        imgSeven.setImageURI(list[6])
//                        imagesEncodedList!!.add(6, list[6])
                    }
                if (list.isNotEmpty() && list.size == 8)
                    if (list[7].toString().isNotEmpty()){
                        imgEight.setImageURI(list[7])
//                        imagesEncodedList!!.add(7, list[7])
                    }


                for (image in list){

                    print(image.toString())
                }

            },
            peekHeight = 1600,
            showTitle = false,
            selectMaxCount = 8,
            completeButtonText = "Certo",
            emptySelectionText = "Não seleccionada")
            .create(this)
        kwikPicker.show(supportFragmentManager)
    }


    private fun biuldImage(imageView: ImageView, uri: Uri){
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_camera_icon)
            .into(imageView)
    }

}
