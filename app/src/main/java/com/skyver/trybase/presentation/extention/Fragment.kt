package com.skyver.trybase.presentation.extention

import android.content.Context
import android.content.res.Resources

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.skyver.trybase.presentation.platform.BaseFragment
import com.skyver.trybase.presentation.MainActivity
import kotlinx.android.synthetic.main.content.*


inline fun <reified T : ViewModel> Fragment.viewModel(factory: ViewModelProvider.Factory, body: T.() -> Unit): T {
    val vm = ViewModelProviders.of(this, factory)[T::class.java]
    vm.body()
    return vm
}


val BaseFragment.viewContainer: View get() = (activity as MainActivity).my_nav_host_fragment.view!!

val BaseFragment.appContext: Context get() = activity?.applicationContext!!

fun BaseFragment.fromResource(res: Int): String {
    return try {
        activity?.applicationContext?.getString(res) ?: ""
    } catch (e: Resources.NotFoundException) {
        ""
    }
}