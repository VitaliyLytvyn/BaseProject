package com.skyver.trybase.presentation.extention

import android.Manifest
import android.content.Context
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import pub.devrel.easypermissions.EasyPermissions


fun Fragment.isConnected() = activity != null && activity!!.isConnected()

fun Context.isConnected(): Boolean {
    val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    return cm != null && isConnected(cm)
}

fun isConnected(cm: ConnectivityManager): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            if (hasTransport(NetworkCapabilities.TRANSPORT_WIFI) or
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true
            }
        }
    } // absolutely needed for all versions below 23
    else {
        cm.activeNetworkInfo?.run {
            if (type == ConnectivityManager.TYPE_WIFI ||
                type == ConnectivityManager.TYPE_MOBILE) {
                return true
            }
        }
    }
    return false
}

fun Fragment.isLocationEnabled(): Boolean {
    activity ?: return false
    return activity!!.isLocationEnabled()
}

fun Context.isLocationEnabled(): Boolean {
    val lm = getSystemService(Context.LOCATION_SERVICE) as? LocationManager
    lm ?: return false

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        lm.isLocationEnabled // This is new method provided in API 28
    } else {
        lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }
}

fun Fragment.hasLocationPermissions(): Boolean {
    return activity?.hasLocationPermissions() ?: false
}

fun Context.hasLocationPermissions(): Boolean {
    return EasyPermissions.hasPermissions(this, Manifest.permission.ACCESS_FINE_LOCATION)//*vararg  mismatch
    //return EasyPermissions.hasPermissions(this, *LOCATION)//*vararg  mismatch
}

fun checkPlayServices(context: Context): Boolean {
    val apiAvailability = GoogleApiAvailability.getInstance()
    val resultCode = apiAvailability.isGooglePlayServicesAvailable(context)
    return resultCode == ConnectionResult.SUCCESS
}
