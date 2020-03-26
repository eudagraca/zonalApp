package mz.co.zonal.utils

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.IntentSender
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*

class GpsUtils(private val context: Context) {
    private val mSettingsClient: SettingsClient = LocationServices.getSettingsClient(context)
    private val mLocationSettingsRequest: LocationSettingsRequest
    private val locationManager: LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    private val locationRequest: LocationRequest = LocationRequest.create()

    init{
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 10 * 1000
        locationRequest.fastestInterval = 2 * 1000
        val builder= LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        mLocationSettingsRequest = builder.build()
        builder.setAlwaysShow(true)
    }

    // method for turn on GPS
    fun turnGPSOn(onGpsListener:onGpsListener) {
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            onGpsListener.gpsStatus(true)
        } else {
            mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(context as Activity) {
                    // GPS is already enable, callback GPS status through listener
                    onGpsListener.gpsStatus(true)
                }
                .addOnFailureListener(context) { e ->
                    when ((e as ApiException).statusCode) {
                        LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                val rae = e as ResolvableApiException
                                rae.startResolutionForResult(context, Constants.GPS_REQUEST)
                            } catch (sie: IntentSender.SendIntentException) {
                                Log.i(TAG, "PendingIntent unable to execute request.")
                            }
                        LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                            val errorMessage = ("Location settings are inadequate, and cannot be " + "fixed here. Fix in Settings.")
                            Log.e(TAG, errorMessage)
                            Toast.makeText(context as Activity, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }
    }

    interface onGpsListener {
        fun gpsStatus(isGPSEnable:Boolean)
    }
}