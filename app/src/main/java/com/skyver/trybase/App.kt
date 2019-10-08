package com.skyver.trybase


import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.skyver.trybase.di.AppInjector
import dagger.android.HasAndroidInjector
import com.skyver.trybase.presentation.platform.CrashReportingTree
import dagger.android.DispatchingAndroidInjector
import timber.log.Timber
import javax.inject.Inject


class App : MultiDexApplication(), HasAndroidInjector {

    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    override fun androidInjector() = dispatchingAndroidInjector

    override fun onCreate() {
        super.onCreate()
        initTimber()

        ////absolutely needed for API versions < 21
        if (GoogleApiAvailability.getInstance() != null) {
            try {
                ProviderInstaller.installIfNeeded(this)
            } catch (e: GooglePlayServicesRepairableException) {
                e.printStackTrace()
            } catch (e: GooglePlayServicesNotAvailableException) {
                e.printStackTrace()
            }catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        //for not crash using vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)

        AppInjector.init(this)
    }

    private fun initTimber() {
        Timber.plant(
            if (BuildConfig.DEBUG)
                Timber.DebugTree()
            else
                CrashReportingTree()
        )
    }
}

