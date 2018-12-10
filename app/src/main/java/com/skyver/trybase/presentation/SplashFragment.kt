package com.skyver.trybase.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.skyver.trybase.R
import com.skyver.trybase.presentation.platform.BaseFragment
import timber.log.Timber
import timber.log.Timber.e

class SplashFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.splash_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkPlayServices()) {
            e("SplashFragment checkPlayServices AVAILABLE")
//            // Start home fragment
//            findNavController()
//                .navigate(R.id.action_splashFragment_to_homeFragment)
        } else {
            e("SplashFragment checkPlayServices NOT AVAILABLE")
        }

    }

    companion object {
        const val PLAY_SERVICES_RESOLUTION_REQUEST = 4794
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    fun checkPlayServices(): Boolean {
        val apiAvailability = GoogleApiAvailability.getInstance()
        val resultCode = apiAvailability.isGooglePlayServicesAvailable(activity)
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(activity, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                    .show()
            } else {
                Timber.i("This device is not supported.")
                //activity?.finish()
            }
            return false
        }
        return true
    }
}