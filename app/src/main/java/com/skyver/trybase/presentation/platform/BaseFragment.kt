package com.skyver.trybase.presentation.platform

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.skyver.trybase.presentation.extention.appContext
import kotlinx.android.synthetic.main.content.*
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.skyver.trybase.AppClass
import com.skyver.trybase.R
import com.skyver.trybase.di.ApplicationComponent
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.presentation.MainActivity
import timber.log.Timber.e
import javax.inject.Inject


abstract class BaseFragment : Fragment() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        (activity?.application as AppClass).appComponent
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var authenticator: Authenticator//todo auth

    open fun onBackPressed() {}

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        //Inject the fragment inside Dagger 2 dependency graph
        appComponent.inject(this)
    }

    //todo check behaviour of hiding progress when fragment is on stop - onStop callback doesn't work
    // due to interference with previous fragment's lifecycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        e("$this onCreate() -> hideProgress")
        hideProgress()
    }

    internal fun firstTimeCreated(savedInstanceState: Bundle?) = savedInstanceState == null

    internal fun showProgress() = progressStatus(View.VISIBLE)

    internal fun hideProgress() = progressStatus(View.GONE)

    private fun progressStatus(viewStatus: Int) {
        if (activity == null || activity !is MainActivity) return //safety check
        with(activity as MainActivity) {
            this.progressBarContainer?.let {
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

   internal fun notifyWithAction( message: String, @StringRes actionText: Int, action: () -> Any) {
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
}