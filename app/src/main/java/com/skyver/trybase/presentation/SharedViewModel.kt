package com.skyver.trybase.presentation

import androidx.lifecycle.MutableLiveData
import com.skyver.trybase.presentation.platform.BaseViewModel
import javax.inject.Inject

class SharedViewModel @Inject constructor() : BaseViewModel() {
    val shared = MutableLiveData<String>()

    fun share(item: String) {
        shared.value = item
    }
}
