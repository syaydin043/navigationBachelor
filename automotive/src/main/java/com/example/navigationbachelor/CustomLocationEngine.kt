package com.mapbox.navigation.examples.turnbyturn

import android.app.PendingIntent
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import com.mapbox.android.core.location.LocationEngine
import com.mapbox.android.core.location.LocationEngineCallback
import com.mapbox.android.core.location.LocationEngineRequest
import com.mapbox.android.core.location.LocationEngineResult
import java.lang.Exception

class CustomLocationEngine(context: Context) : LocationEngine {

    private var callbacks =
        mutableMapOf<LocationEngineCallback<LocationEngineResult>, LocationListener>()
    private var lm = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

//    override fun getLastLocation(callback: LocationEngineCallback<LocationEngineResult>) {
//        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
//            callback.onSuccess(LocationEngineResult.create(it))
//        }.let {
//            callback.onFailure(Exception("getLastKnownLocation Error"))
//        }
//    }

    override fun getLastLocation(callback: LocationEngineCallback<LocationEngineResult>) {
        lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
            callback.onSuccess(LocationEngineResult.create(it))
        } ?: run {
            callback.onFailure(Exception("getLastKnownLocation Error"))
        }
    }


    override fun requestLocationUpdates(
        request: LocationEngineRequest,
        callback: LocationEngineCallback<LocationEngineResult>,
        looper: Looper?
    ) {
        looper?.let {
            val locationListener = LocationListener { location ->
                callback.onSuccess(LocationEngineResult.create(location))
            }
            callbacks[callback] = locationListener
            it.run {
                lm.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    request.interval,
                    request.displacement,
                    locationListener
                )
            }
        }
    }

    override fun requestLocationUpdates(
        request: LocationEngineRequest,
        pendingIntent: PendingIntent?
    ) {
        TODO("Not yet implemented")
    }

    override fun removeLocationUpdates(callback: LocationEngineCallback<LocationEngineResult>) {
        callbacks[callback]?.let {
            lm.removeUpdates(it)
            callbacks.remove(callback)
        }
    }

    override fun removeLocationUpdates(pendingIntent: PendingIntent?) {
        TODO("Not yet implemented")
    }
}