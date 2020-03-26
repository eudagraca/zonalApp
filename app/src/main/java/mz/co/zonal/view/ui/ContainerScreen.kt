package mz.co.zonal.view.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.core.view.isVisible
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_container.*
import mz.co.zonal.R
import mz.co.zonal.service.factory.TypeViewModelFactory
import mz.co.zonal.service.factory.UserViewModelFactory
import mz.co.zonal.service.model.Category
import mz.co.zonal.service.model.Session
import mz.co.zonal.service.model.Type
import mz.co.zonal.service.model.User
import mz.co.zonal.utils.CategoryViewModelFactory
import mz.co.zonal.utils.Constants
import mz.co.zonal.utils.Coroutine
import mz.co.zonal.utils.GpsUtils
import mz.co.zonal.view.callback.AuthListener
import mz.co.zonal.view.callback.OnDrawerInteractionListener
import mz.co.zonal.view.ui.category.BottomSheet
import mz.co.zonal.view.ui.product.FeedsFragment
import mz.co.zonal.viewmodel.CategoryViewModel
import mz.co.zonal.viewmodel.TypeViewModel
import mz.co.zonal.viewmodel.UserViewModel
import org.jetbrains.anko.toast
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*


class ContainerScreen : AppCompatActivity(), FeedsFragment.OnFragmentInteractionListener,
    BottomSheet.OnClickCategory, KodeinAware, AuthListener {

    override val kodein by kodein()
    var drawer: DrawerLayout? = null
    private var mFusedLocationClient: FusedLocationProviderClient? = null
    private var wayLatitude = 0.0
    private var wayLongitude = 0.0
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var isGPS = false
    private var bottomSheetFragment: BottomSheet? = null

    //Categories
    private lateinit var categoryViewModel: CategoryViewModel
    private val categoryFactory: CategoryViewModelFactory by instance()
    private var adapterCategories: ArrayAdapter<Category>? = null

    //Type
    private lateinit var typeViewModel: TypeViewModel
    private val typeFactory: TypeViewModelFactory by instance()
    private var adapterType: ArrayAdapter<Type>? = null

    //User
    private val factory: UserViewModelFactory by instance()
    var userViewModel: UserViewModel? = null

    private var onClickListener: OnDrawerInteractionListener? = null

    private val session: Session by instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        drawer =
            findViewById<View>(R.id.drawer_layout) as DrawerLayout
        userViewModel = ViewModelProvider(this, factory).get(UserViewModel::class.java)
        categoryViewModel =
            ViewModelProvider(this, categoryFactory).get(CategoryViewModel::class.java)
        typeViewModel = ViewModelProvider(this, typeFactory).get(TypeViewModel::class.java)

        userViewModel?.authListener = this
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun sendTeam(playerResponse: Category) {
        bottomSheetFragment = BottomSheet.newInstance(this)
        bottomSheetFragment?.show(supportFragmentManager, bottomSheetFragment?.tag)
    }

    override fun categorySelected(category: Category) {

    }

    private fun getLocation() {
        if ((ActivityCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                baseContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf<String>(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                Constants.LOCATION_REQUEST
            )
        } else {
            mFusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
                if (location != null) {
                    wayLatitude = location.latitude
                    wayLongitude = location.longitude

                    val addresses = getAddress(wayLatitude, wayLongitude)

                    val country = addresses[0].countryName
                    val province = addresses[0].adminArea
                    val city = addresses[0].subAdminArea

                    userViewModel?.userLogged?.observe(this, androidx.lifecycle.Observer {
                        if (it != null) {
                            if ((it.latitude != wayLatitude && it.city != city) && (wayLatitude != 0.0)) {
                                val user = User(
                                    country = country, province = province, city = city,
                                    latitude = wayLatitude, longitude = wayLongitude, id = it.id
                                )
                                userViewModel?.userLiveData?.observe(
                                    this,
                                    androidx.lifecycle.Observer {})
                                userViewModel?.setCoordinators(user)

                                if (it.token == null || it.token != session.token){
                                    userViewModel?.setToken(session.token)
                                }
                            }
                        }
                    })
                    session.setLatLong(wayLatitude.toString(), wayLongitude.toString())
                } else {
                    mFusedLocationClient?.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        Looper.myLooper()
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setupGps()

        if (isGPS) {
            getLocation()
        } else {
            getLocation()
        }
    }

    override fun onResume() {
        super.onResume()
        if (isGPS) {
            getLocation()
        } else {
            getLocation()
        }
    }


    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            Constants.LOCATION_REQUEST -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    mFusedLocationClient?.lastLocation?.addOnSuccessListener(this) { location ->
                        if (location != null) {
                            wayLatitude = location.latitude
                            wayLongitude = location.longitude
                        } else {
                            mFusedLocationClient?.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.myLooper()
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GPS_REQUEST) {
                isGPS = true // flag maintain before get location
            }
        }
    }

    private fun setupGps() {
        with(locationRequest, {
            LocationRequest.create()
            this?.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            this?.interval = Constants.INTERVAL_UPDATE
            this?.fastestInterval = Constants.FAST_UPDATE
            this?.smallestDisplacement = 10F
        })// 10 seconds

        GpsUtils(this).turnGPSOn(object : GpsUtils.onGpsListener {
            override fun gpsStatus(isGPSEnable: Boolean) { // turn on GPS
                isGPS = isGPSEnable
            }
        })

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.locations.isEmpty()) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {
                        wayLatitude = location.latitude
                        wayLongitude = location.longitude
                        if (mFusedLocationClient != null) {
                            mFusedLocationClient?.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }
        }
    }

    private fun getAddress(lat: Double, long: Double): List<Address> {
        val geoCoder = Geocoder(this, Locale.getDefault())
        return geoCoder.getFromLocation(lat, long, 1)
    }

    override fun onStarted() {
    }

    override fun onSuccess(user: Any) {
    }

    override fun onFailure(message: String) {
    }

    @SuppressLint("WrongConstant")
    fun openDrawer(onClickListener: OnDrawerInteractionListener) {
        val drawer =
            findViewById<View>(R.id.drawer_layout) as DrawerLayout
        drawer.openDrawer(GravityCompat.END)
        categoryViewModel.fetchCategories()
        typeViewModel.fetchTypes()
        populateSpinners()

        mb_cat_selected.text = null
        mb_status_selected.text = null
        mb_cat_selected.visibility = View.VISIBLE
        mb_status_selected.visibility = View.VISIBLE
        mbDone.setOnClickListener {

            if (mb_status_selected.isVisible && mb_cat_selected.isVisible) {
                onClickListener.sendData(
                    category = categories_spinner.getSpinner().selectedItem as Category?,
                    type = type_spinner.getSpinner().selectedItem as Type?,
                    min = tie_min.text?.toString(),
                    max = tie_max.text?.toString()
                )

            }else if (!mb_status_selected.isVisible && mb_cat_selected.isVisible){
                onClickListener.sendData(
                    category = categories_spinner.getSpinner().selectedItem as Category?,
                    min = tie_min.text?.toString(),
                    max = tie_max.text?.toString()
                )
            }else if (mb_status_selected.isVisible &&  !mb_cat_selected.isVisible){
                onClickListener.sendData(
                    type = type_spinner.getSpinner().selectedItem as Type?,
                    min = tie_min.text?.toString(),
                    max = tie_max.text?.toString()
                )
            }else{
                onClickListener.sendData(
                    min = tie_min.text?.toString(),
                    max = tie_max.text?.toString()
                )
            }

            closeDrawer()
        }

        categories_spinner.getSpinner().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mb_cat_selected.visibility = View.GONE
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mb_cat_selected.text = categories_spinner.getSelectedItem().toString()
                mb_cat_selected.visibility = View.VISIBLE
            }

        }

        mb_reset.setOnClickListener {
            mb_cat_selected.visibility = View.GONE
            mb_status_selected.visibility = View.GONE
            tie_max.text = null
            tie_min.text = null
        }

        mb_cat_selected.setOnClickListener {
            mb_cat_selected.visibility = View.GONE
            mb_cat_selected.text = null
        }

        mb_status_selected.setOnClickListener {
            mb_status_selected.visibility = View.GONE
            mb_status_selected.text = null
        }

        type_spinner.getSpinner().onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                mb_status_selected.visibility = View.GONE
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mb_status_selected.text = type_spinner.getSelectedItem().toString()
                mb_status_selected.visibility = View.VISIBLE
            }
        }

    }

    @SuppressLint("WrongConstant")
    fun closeDrawer() {
        drawer?.closeDrawer(GravityCompat.END)
    }

    fun lockDrawer() {
        drawer?.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
    }

    fun unlockDrawer() {
        drawer?.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    private fun setupCategories(it: List<Category>) {
        adapterCategories = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
        adapterCategories!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(categories_spinner, {
            setAdapter(adapterCategories!!)
        })
    }

    private fun setupType(it: List<Type>) {
        adapterType = ArrayAdapter(this, android.R.layout.simple_spinner_item, it)
        adapterType!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        with(type_spinner, {
            setAdapter(adapterType!!)
            isSelected = false
        })
    }

    private fun populateSpinners() = Coroutine.main {
        categoryViewModel.categoryLiveData.observe(this, androidx.lifecycle.Observer {
            setupCategories(it)
        })

        typeViewModel.types.observe(this, androidx.lifecycle.Observer {
            setupType(it)
        })
    }
    }
