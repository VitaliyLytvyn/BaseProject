package com.skyver.trybase.presentation.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.skyver.trybase.presentation.extention.Failure
import kotlinx.coroutines.*
import timber.log.Timber.e


/**
 * Base ViewModel class with default Failure handling.
 * @see ViewModel
 * @see Failure
 */
abstract class BaseViewModel : ViewModel() {

    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will cancel all coroutines started by this ViewModel.
     */
    //private val viewModelJob = Job()
    /**
     * This is the job for all coroutines started by this ViewModel.
     *
     * Cancelling this job will NOT cancel all coroutines started by this ViewModel - needed for async try catch.
     */
    private val viewModelJob = SupervisorJob()
    /**
     * This is the main scope for all coroutines launched by MainViewModel.
     *
     * Since we pass viewModelJob, you can cancel all coroutines launched by uiScope by calling
     * viewModelJob.cancel()
     */
    val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    var failure: MutableLiveData<Failure> = MutableLiveData()

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()

        //this is alternative: https://proandroiddev.com/kotlin-coroutines-patterns-anti-patterns-f9d12984c68e
        //uiScope.coroutineContext.cancelChildren()
    }
}