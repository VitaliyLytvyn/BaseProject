package com.skyver.trybase


import androidx.appcompat.app.AppCompatDelegate
import androidx.multidex.MultiDexApplication
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.GooglePlayServicesNotAvailableException
import com.google.android.gms.common.GooglePlayServicesRepairableException
import com.google.android.gms.security.ProviderInstaller
import com.skyver.trybase.di.ApplicationComponent
import com.skyver.trybase.di.ApplicationModule
import com.skyver.trybase.di.DaggerApplicationComponent
import com.skyver.trybase.presentation.platform.CrashReportingTree
import timber.log.Timber


//class AppClass : Application() {
class AppClass : MultiDexApplication() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

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
            }
        }

        injectMembers()

        //for not crash using vector drawables
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }
    private fun injectMembers() = appComponent.inject(this)

    private fun initTimber() {
        Timber.plant(
            if (BuildConfig.DEBUG)
                Timber.DebugTree()
            else
                CrashReportingTree()
        )
    }
}

