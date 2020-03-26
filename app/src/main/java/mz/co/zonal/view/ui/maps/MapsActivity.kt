package mz.co.zonal.view.ui.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.RequestBuilder
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_maps.*
import mz.co.zonal.R
import mz.co.zonal.service.model.GoogleMapsDTO
import mz.co.zonal.service.model.Session
import mz.co.zonal.utils.Constants.LOCATION_REQUEST
import okhttp3.OkHttpClient
import okhttp3.Request
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.lang.Exception


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, KodeinAware {

    override val kodein by kodein()
    private lateinit var mMap: GoogleMap
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var name: String = ""
    private val session: Session by instance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        latitude = intent.getDoubleExtra("latitude", 0.0)
        longitude = intent.getDoubleExtra("longitude", 0.0)
        name = intent.getStringExtra("name")!!

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location =
            LatLng(latitude, longitude)

        val ownLocation = session.lat?.toDouble()?.let { lat ->
            session.long?.toDouble()?.let { long -> LatLng(lat, long) }
        }


        val circleOptions = CircleOptions()
            .center(ownLocation) //set center
            .radius(300.0) //set radius in meters
            .fillColor(0x40ff0000) //semi-transparent
            .strokeColor(0x40ff0000)
            .strokeWidth(1f)

        mMap.addCircle(circleOptions)

        googleMap.addMarker(
            MarkerOptions().position(location)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                .title(name)
        )

        googleMap.addMarker(
            ownLocation?.let {
                MarkerOptions().position(it)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                    .title("Minha localização")
            }
        )

        my_Location.setOnClickListener {
            getMyLocation(ownLocation!!)
        }

        Log.d("GoogleMap", "before URL")
        val URL = getDirections(ownLocation!!, location)
        Log.d("GoogleMap", "URL : $URL")
        GetDirection(URL.toString()).execute()


        other_location.setOnClickListener {
            if (ownLocation != null) {
                getMyLocation(location)
            }
        }

        googleMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(location, 15.0f)
        )

        try {
            // Customize the styling of the base map using a JSON object defined
            // in a raw resource file.
            val success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this, R.raw.map_style
                )
            )
            if (!success) {
                Log.e("MAP", "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e("MAP", "Can't find style. Error: ", e)
        }
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_REQUEST
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        when (requestCode) {
            LOCATION_REQUEST -> if (grantResults.isNotEmpty()
                && grantResults[0]
                == PackageManager.PERMISSION_GRANTED
            ) {
                enableMyLocation()
                return
            }
        }
    }

    private fun getMyLocation(location: LatLng) {
        val latLng = LatLng(location.latitude, location.longitude)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15.0f)
        mMap.animateCamera(cameraUpdate)
    }

    private fun getDirections(origin: LatLng, destination: LatLng): String {
        val mode = "driving"
        val result = "https://maps.googleapis.com/maps/api/directions/json?origin=${origin.latitude},${origin.longitude}&destination=${destination.latitude},${destination.longitude}&${mode}&key=" + R.string.google_maps_key

        return result
    }



    private inner class GetDirection(val url : String) : AsyncTask<Void,Void,List<List<LatLng>>>(){
        override fun doInBackground(vararg params: Void?): List<List<LatLng>> {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            val response = client.newCall(request).execute()
            val data = response.body()!!.string()
            Log.d("GoogleMap" , " data : $data")
            val result =  ArrayList<List<LatLng>>()
            try{
                val respObj = Gson().fromJson(data, GoogleMapsDTO::class.java)

                val path =  ArrayList<LatLng>()

                for (i in 0 until respObj.routes[0].legs[0].steps.size){
//                    val startLatLng = LatLng(respObj.routes[0].legs[0].steps[i].start_location.lat.toDouble()
//                            ,respObj.routes[0].legs[0].steps[i].start_location.lng.toDouble())
//                    path.add(startLatLng)
//                    val endLatLng = LatLng(respObj.routes[0].legs[0].steps[i].end_location.lat.toDouble()
//                            ,respObj.routes[0].legs[0].steps[i].end_location.lng.toDouble())
                    path.addAll(decodePolyline(respObj.routes[0].legs[0].steps[i].polyline.points))
                }
                result.add(path)
            }catch (e:Exception){
                e.printStackTrace()
            }
            return result
        }

        override fun onPostExecute(result: List<List<LatLng>>) {
            val lineoption = PolylineOptions()
            for (i in result.indices){
                lineoption.addAll(result[i])
                lineoption.width(10f)
                lineoption.color(Color.BLUE)
                lineoption.geodesic(true)
            }
            mMap.addPolyline(lineoption)
        }
    }

    public fun decodePolyline(encoded: String): List<LatLng> {

        val poly = ArrayList<LatLng>()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val latLng = LatLng((lat.toDouble() / 1E5),(lng.toDouble() / 1E5))
            poly.add(latLng)
        }

        return poly
    }


}
