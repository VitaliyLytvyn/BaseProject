package com.skyver.trybase


import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.skyver.trybase.di.ApplicationComponent
import com.skyver.trybase.di.ApplicationModule
import com.skyver.trybase.di.DaggerApplicationComponent
import com.skyver.trybase.presentation.platform.CrashReportingTree
import timber.log.Timber


class AppClass : Application() {

    val appComponent: ApplicationComponent by lazy(mode = LazyThreadSafetyMode.NONE) {
        DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        initTimber()
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

