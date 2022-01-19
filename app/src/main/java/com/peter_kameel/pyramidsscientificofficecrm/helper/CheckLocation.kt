package com.peter_kameel.pyramidsscientificofficecrm.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.peter_kameel.pyramidsscientificofficecrm.util.Massages
import kotlinx.coroutines.*

object CheckLocation {

    suspend fun getCurrent1location(ctx: Context,onSuccess: (Location) -> Unit, onError: (String) -> Unit) {
        var gpsEnabled = false
        //get location client
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(ctx)
        //set location manger
        val locationManger = ctx.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        withContext(CoroutineScope(Dispatchers.IO).coroutineContext) {
            try {
                gpsEnabled = locationManger.isProviderEnabled(LocationManager.GPS_PROVIDER)
            } catch (ex: Exception) {
                CoroutineScope(Dispatchers.Main).launch {
                    onError(ex.toString())
                }
            }
            if (gpsEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        ctx,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        ctx,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    fusedLocationClient.getCurrentLocation(
                        LocationRequest.PRIORITY_HIGH_ACCURACY,
                        CancellationTokenSource().token
                    )
                        .addOnSuccessListener { location: Location? ->
                            CoroutineScope(Dispatchers.Main).launch {
                                location?.let { onSuccess(it) }
                            }
                        }
                } else {
                    //Request The Permission
                    ActivityCompat.requestPermissions(
                        ctx as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001
                    )
                }
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    onError(Massages.EnableGPS)
                }
            }
        }
    }

    suspend fun getDistanceTotLocation(ctx: Context, lat: Double, lon: Double,
        onSuccess: (Double) -> Unit,
        onError: (String) -> Unit){
        CoroutineScope(Dispatchers.Main).launch{
            getCurrent1location(ctx,onSuccess = {
                val results = FloatArray(1)
                Location.distanceBetween(lat, lon, it.latitude, it.longitude, results)
                onSuccess(results[0].toDouble())
            },onError = {
                onError(it)
            })
        }
    }
}


