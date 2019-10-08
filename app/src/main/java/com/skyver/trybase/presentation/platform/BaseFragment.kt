package com.skyver.trybase.presentation.platform

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.skyver.trybase.presentation.extention.appContext
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import com.skyver.trybase.R
import com.skyver.trybase.di.Injectable
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.presentation.MainActivity
import com.skyver.trybase.presentation.extention.hasLocationPermissions
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.activity_main.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber.d
import timber.log.Timber.e
import javax.inject.Inject


abstract class BaseFragment : Fragment(), EasyPermissions.PermissionCallbacks ,  Injectable {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var authenticator: Authenticator//todo auth

    @Inject
    lateinit var prefs: PreferenceHelper

    override fun onAttach(context: Context) {
        super.onAttach(context)
        //Inject the fragment inside Dagger 2 dependency graph
        // must be called before super.onCreate():
        AndroidSupportInjection.inject(this)
    }

    //todo check behaviour of hiding progress when fragment is on stop - onStop callback doesn't work
    // due to interference with previous fragment's lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        e("$this onCreate() -> hideProgress")
        hideProgress()
    }

    override fun onStart() {
        super.onStart()
        e("onStart $this")
    }

    override fun onPause() {
        super.onPause()
        e("onPause $this")
    }

    override fun onStop() {
        super.onStop()
        hideKeyboard()
        e("onStop $this")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        e("onDestroyView $this")
    }

    override fun onDestroy() {
        super.onDestroy()
        e("onDestroy $this")
    }

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        if (activity == null || activity !is MainActivity) return //safety check

        with(activity as? MainActivity) {
            this?.progressBarContainer?.let {
                if (it.visibility == viewStatus) return@with // prevents unneeded manipulations

                it.visibility = viewStatus
                if (viewStatus == View.VISIBLE) {
                    progressBarContainer.setOnClickListener {}//disable clicks on presentation under progress bar
                } else
                    progressBarContainer.setOnClickListener(null)//enable clicks on presentation under progress bar
            }

            //setEnabledAll(my_nav_host_fragment.presentation!!, viewStatus != View.VISIBLE)
        }

    }

    //private fun hideKeyboard(context: Context?, view: View?) {
    private fun hideKeyboard() {
        activity ?: return
        view ?: return
        val imm = activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(view!!.windowToken, 0)
    }

    //check it - alternative to setOnClickListener to disable clicks on presentation under progress bar
    private fun setEnabledAll(v: View, enabled: Boolean) {
        v.isEnabled = enabled
        v.isFocusable = enabled
        if (v is ViewGroup) {
            for (i in 0 until v.childCount)
                setEnabledAll(v.getChildAt(i), enabled)
        }
    }

    internal fun notify(@StringRes message: Int) {
        my_nav_host_fragment?.view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    internal fun notify(message: String) {
        my_nav_host_fragment?.view?.let {
            Snackbar.make(it, message, Snackbar.LENGTH_SHORT).show()
        }
    }

    internal fun notifyWithAction(@StringRes message: Int, @StringRes actionText: Int, action: () -> Any) {
        val s = activity?.getString(message) ?: return
        notifyWithAction(s, actionText, action)
    }

    internal fun notifyWithAction(message: String, @StringRes actionText: Int, action: () -> Any) {
        my_nav_host_fragment?.view?.let {
            val snackBar = Snackbar.make(it, message, Snackbar.LENGTH_INDEFINITE)
            snackBar.setAction(actionText) { _ -> action.invoke() }
            snackBar.setActionTextColor(
                ContextCompat.getColor(
                    appContext, R.color.colorTextPrimary
                )
            )
            snackBar.show()
        }
    }

    //for Runtime permission
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    //for Runtime permission
    override fun onPermissionsDenied(requestCode: Int, @NonNull perms: List<String>) {
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    //for Runtime permission
    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}


    //todo for Runtime permission IN PARTICULAR FRAGMENT FOR PARTICULAR PERMISSION
    //todo - do or do NOT something after return from settings
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            //do something after return from settings
//            if (hasLocationPermissions()) {
//                // Have permissions, do the thing!
//            }
        }
    }

    //todo for Runtime permission IN PARTICULAR FRAGMENT FOR PARTICULAR PERMISSION
    @AfterPermissionGranted(RC_LOCATION_PERM)
    fun locationAndContactsTask() {
        if (hasLocationPermissions()) {
            // Have permissions, do the thing!
            d("have permission - > do the thing")
        } else {
            e("have NO permission")
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                "rationale for location",
                RC_LOCATION_PERM,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    //todo for Runtime permission IN PARTICULAR FRAGMENT FOR PARTICULAR PERMISSION
    companion object {
        const val RC_LOCATION_PERM = 998
    }

}