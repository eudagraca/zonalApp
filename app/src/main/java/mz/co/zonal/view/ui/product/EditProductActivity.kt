package mz.co.zonal.view.ui.product

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import co.csadev.kwikpicker.KwikPicker
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_sell_product.*
import kotlinx.android.synthetic.main.form.*
import mz.co.zonal.R
import mz.co.zonal.service.factory.*
import mz.co.zonal.service.model.*
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.utils.Utils
import mz.co.zonal.utils.converters.FilePart.Companion.prepareFilePart
import mz.co.zonal.utils.loadImageByte
import mz.co.zonal.view.ui.category.BottomSheet
import mz.co.zonal.viewmodel.*
import okhttp3.MultipartBody
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class EditProductActivity : AppCompatActivity(), KodeinAware, TextWatcher,
    AdapterView.OnItemSelectedListener, BottomSheet.OnClickCategory {

    override val kodein by kodein()
    private val factoryProduct: ProductViewModelFactory by instance()
    private val typeFactory: TypeViewModelFactory by instance()
    private val brandsFactory: BrandViewModelFactory by instance()
    private val currencyFactory: CurrencyViewModelFactory by instance()
    private lateinit var productViewModel: ProductViewModel
    private lateinit var typeViewModel: TypeViewModel
    private lateinit var brandViewModel: BrandViewModel
    private lateinit var currencyViewModel: CurrencyViewModel
    private lateinit var userViewModel: UserViewModel
    private val userFactory: UserViewModelFactory by instance()
    private var bottomSheetFragment: BottomSheet? = null

    //Old images
    var imagesList = arrayListOf<Uri>()
    //Type
    private var adapterType: ArrayAdapter<Type>? = null
    //Brand
    private var adapterBrand: ArrayAdapter<Brand>? = null
    //Currency
    private var adapterCurrency: ArrayAdapter<Currency>? = null
    // List of file parts
    private var parts = arrayListOf<MultipartBody.Part>()
    //Category
    private var category: Category? = null

    //Object Parcel
    var idProduct: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell_product)

        typeViewModel = ViewModelProvider(this, typeFactory).get(TypeViewModel::class.java)
        brandViewModel = ViewModelProvider(this, brandsFactory).get(BrandViewModel::class.java)
        currencyViewModel =
            ViewModelProvider(this, currencyFactory).get(CurrencyViewModel::class.java)
        productViewModel = ViewModelProvider(this, factoryProduct).get(ProductViewModel::class.java)
        userViewModel = ViewModelProvider(this, userFactory).get(UserViewModel::class.java)

        val bundle = intent.extras
        idProduct = bundle?.getLong(Constants.PRODUCT, 0)

        brandViewModel.fetchBrands()
        currencyViewModel.fetchCurrencies()
        typeViewModel.fetchTypes()

        setupSpinners()

        tied_name.addTextChangedListener(this)
        tied_desc.addTextChangedListener(this)
        tied_price.addTextChangedListener(this)
        type_spinner.getSpinner().onItemSelectedListener = this
        currency_spinner.getSpinner().onItemSelectedListener = this
        bran_spinner.getSpinner().onItemSelectedListener = this

        til_price.isErrorEnabled = true
        til_name.isErrorEnabled = true
        til_desc.isErrorEnabled = true
//        save_product.isEnabled = !isInvalid()

        fetchProduct()
        openGallery!!.setOnClickListener { selectImage()?.show(supportFragmentManager) }


        selectCategory!!.setOnClickListener {
            bottomSheetFragment = BottomSheet.newInstance(this)
            bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment?.tag)
        }

        save_product.setOnClickListener {
            userViewModel.userLogged.observe(this, Observer {
                enableFields(false)
                productViewModel.productLiveData.observe(this, Observer { product ->
                    if (product != null) {
                        closeKeyboard(sell_product)
                        enableFields(true)
                        onBackPressed()
                        finish()
                    } else {
                        enableFields(false)
                    }
                })
                val product = setProduct(it, idProduct!!)
                productViewModel.updateProduct(product, parts)
            })
        }
    }

    private fun viewImages(list: List<String>) {
        if (list.isNotEmpty() && list.isNotEmpty())
            if (list[0].isNotEmpty()) {
                loadImageByte(imgOne, list[0])
            }

        if (list.isNotEmpty() && list.size > 1)
            if (list[1].toString().isNotEmpty()) {
                loadImageByte(imgTwo, list[1])
            }

        if (list.isNotEmpty() && list.size > 2)
            if (list[2].isNotEmpty()) {
                loadImageByte(imgThree, list[2])
            }

        if (list.isNotEmpty() && list.size > 3)
            if (list[3].isNotEmpty()) {
                loadImageByte(imgFour, list[3])
            }

        if (list.isNotEmpty() && list.size > 4)
            if (list[4].isNotEmpty()) {
                loadImageByte(imgFive, list[4])
            }

        if (list.isNotEmpty() && list.size > 5)
            if (list[5].isNotEmpty()) {
                loadImageByte(imgSix, list[5])
            }
    }

    private fun setupSpinners() = Coroutine.main {
        brandViewModel.brands.observe(this, Observer {
            setupBrandSpinner(it)
        })

        typeViewModel.types.observe(this, Observer {
            setupTypesSpinner(it)
        })

        currencyViewModel.currency.observe(this, Observer {
            setupCurrenciesSpinner(it)
        })
    }

    private fun setupTypesSpinner(it: List<Type>) {
        adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
        adapterType!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(type_spinner, {
            setAdapter(adapterType!!)
        })
    }

    private fun setupCurrenciesSpinner(it: List<Currency>) {
        adapterCurrency = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
        adapterCurrency!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(currency_spinner, {
            setAdapter(adapterCurrency!!)
        })
    }

    private fun setupBrandSpinner(it: List<Brand>) {
        adapterBrand = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
        adapterBrand!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(bran_spinner, {
            setAdapter(adapterBrand!!)
        })
    }

    private fun fetchProduct() {
        productViewModel.productLiveData.observe(this, Observer {
            it as Product
            tied_name.setText(it.title)
            tied_desc.setText(it.description)
            tied_price.setText(it.price.toString())
            this.category = it.category
            selectCategory.text = HtmlCompat.fromHtml(
                "Seleccionou <b>${it.category}</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )

            it.imagesByte?.let { it1 -> viewImages(it1) }
            if (it.type != null) {
                val spinnerPosition = adapterType?.getPosition(it.type)
                spinnerPosition?.let { it1 -> type_spinner.getSpinner().setSelection(it1) }
            }

            if (it.brand != null) {
                val spinnerPosition = adapterBrand?.getPosition(it.brand)
                spinnerPosition?.let { it1 -> bran_spinner.getSpinner().setSelection(it1) }
            }

            if (it.currency != null) {
                val spinnerPosition = adapterCurrency?.getPosition(it.currency)
                spinnerPosition?.let { it1 -> currency_spinner.getSpinner().setSelection(it1) }
            }

        })
        productViewModel.productById(idProduct!!)
    }

    private fun selectImage(): KwikPicker? {
        if (ContextCompat.checkSelfPermission(
                this,
                WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this, WRITE_EXTERNAL_STORAGE
                )
            ) {
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(WRITE_EXTERNAL_STORAGE),
                    1
                )
            }
            return null
        }

        return KwikPicker.Builder(
            this@EditProductActivity,
            imageProvider = { imageView, uri ->
                Glide.with(this@EditProductActivity)
                    .load(uri)
                    .into(imageView)
            },
            onMultiImageSelectedListener = { list: ArrayList<Uri> ->

                if (list.isNotEmpty() && list.size > 0)
                    if (list[0].toString().isNotEmpty()) {
                        imagesList.add(0, list[0])
                        buildImage(imgOne, list[0])
                    }

                if (list.isNotEmpty() && list.size > 1)
                    if (list[1].toString().isNotEmpty()) {
                        imagesList.add(1, list[1])
                        buildImage(imgTwo, list[1])
                    }

                if (list.isNotEmpty() && list.size > 2)
                    if (list[2].toString().isNotEmpty()) {
                        imagesList.add(2, list[2])
                        buildImage(imgThree, list[2])
                    }

                if (list.isNotEmpty() && list.size > 3)
                    if (list[3].toString().isNotEmpty()) {
                        imagesList.add(3, list[3])
                        buildImage(imgFour, list[3])
                    }

                if (list.isNotEmpty() && list.size > 4)
                    if (list[4].toString().isNotEmpty()) {
                        imagesList.add(4, list[4])
                        imgFive.setImageURI(list[4])
                    }

                if (list.isNotEmpty() && list.size > 5)
                    if (list[5].toString().isNotEmpty()) {
                        imagesList.add(5, list[5])
                        imgSix.setImageURI(list[5])
                    }

                if (list.size > 0) {
                    for (image in imagesList) {
                        parts.add(
                            prepareFilePart(
                                "files",
                                image
                            )!!
                        )
                    }
                    save_product.isEnabled = !isInvalid()
                }
            },

            peekHeight = Utils.getScreenHeight(),
            showTitle = false,
            selectMaxCount = 6,
            showCamera = true,
            showGallery = true,
            title = "Imagens",
            completeButtonText = "Certo",
            emptySelectionText = "Nenhuma imagem seleccionada"
        )

            .create(this)
    }

    private fun buildImage(imageView: ImageView, uri: Uri) {
        Glide.with(this)
            .load(uri)
            .centerCrop()
            .placeholder(R.drawable.ic_camera_icon)
            .into(imageView)
    }

    override fun afterTextChanged(editable: Editable?) {
        save_product.isEnabled = !isInvalid()
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        when {
            tied_name.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() || s.isEmpty() -> {
                        til_name.error = "\nInsira o nome"
                        return
                    }
                    s.length < 3 -> {
                        til_name.error = "\nMínimo 3 caracteres"
                        Toast.makeText(
                            baseContext,
                            bran_spinner.getSpinner().selectedItem.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                    else -> {
                        til_name.isErrorEnabled = false
                    }
                }
            }
            tied_desc.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() || s.isEmpty() -> {
                        til_desc.error = "\nPreencha a descrição"
                    }
                    s.length < 10 -> {
                        til_desc.error = "\nMínimo 10 caracteres"
                    }
                    else -> {
                        til_desc.isErrorEnabled = false
                        return
                    }
                }
            }
            tied_price.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() -> {
                        til_price.error = "\nPreencha o preço"
                        return
                    }
                    s.toString().toDouble() < 1 -> {
                        til_price.error = "\nPreço inválidooo"
                        return
                    }
                    else -> {
                        til_price.isErrorEnabled = false
                    }
                }
            }
        }
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        when {
            tied_name.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() || s.isEmpty() -> {
                        til_name.error = "\nInsira o nome"
                        return
                    }
                    s.length < 3 -> {
                        til_name.error = "\nMínimo 3 caracteres"
                        return
                    }
                    else -> {
                        til_name.isErrorEnabled = false
                    }
                }
            }
            tied_desc.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() || s.isEmpty() -> {
                        til_desc.error = "\nPreencha a descrição"
                    }
                    s.length < 10 -> {
                        til_desc.error = "\nMínimo 10 caracteres"
                    }
                    else -> {
                        til_desc.isErrorEnabled = false
                        return
                    }
                }
            }
            tied_price.text.hashCode() == s.hashCode() -> {
                when {
                    s.isNullOrEmpty() || s.isNullOrBlank() -> {
                        til_price.error = "\nPreencha o preço"
                        return
                    }
                    s.toString().toDouble() < 1 -> {
                        til_price.error = "\nPreço inválido"
                        return
                    }
                    else -> {
                        til_price.isErrorEnabled = false
                    }
                }
            }
        }
    }

    private fun enableFields(status: Boolean) {
        tied_price.isEnabled = status
        tied_desc.isEnabled = status
        tied_name.isEnabled = status
        save_product.isEnabled = status
        type_spinner.isEnabled = status
        currency_spinner.isEnabled = status
        bran_spinner.isEnabled = status
        openGallery.isEnabled = status
        selectCategory.isEnabled = status
    }

    private fun closeKeyboard(view: View) {
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE)
                as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun close(view: View) {
        onBackPressedDispatcher.onBackPressed()
    }

    private fun isInvalid() =
        (tied_name.text.toString().isEmpty() || tied_name.text.toString().length < 3
                || tied_desc.text.toString().isEmpty() || tied_desc.text.toString().length < 10
                || tied_price.text.toString().isEmpty() || tied_price.text.toString().toDouble() < 0
                || type_spinner.getSpinner().selectedItem == null
                || currency_spinner.getSpinner().selectedItem == null||
                this.category == null
//                || parts.size < 1 || category!!.name.isNullOrEmpty())
                )

    private fun setProduct(user: User, productId: Long): Product {
        return Product(
            id = productId,
            title = tied_name.text.toString(), description = tied_desc.text.toString(),
            price = tied_price.text.toString().toDouble(),
            user = user,
            currency = currency_spinner.getSelectedItem() as Currency,
            type = type_spinner.getSelectedItem() as Type,
            brand = bran_spinner.getSelectedItem() as Brand,
            category = category
        )
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    }

    override fun categorySelected(category: Category) {
        this.category = category
        save_product.isEnabled = !isInvalid()
        if (this.category != null) {
            selectCategory.text = HtmlCompat.fromHtml(
                "Seleccionou <b>${this.category}</b>",
                HtmlCompat.FROM_HTML_MODE_LEGACY
            )
        }
    }


}