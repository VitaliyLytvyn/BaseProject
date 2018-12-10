package com.skyver.trybase.di

import com.skyver.trybase.AppClass
import com.skyver.trybase.di.viewmodel.ViewModelModule
import com.skyver.trybase.presentation.FlowStepFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AppClass)
    fun inject(flowStepFragment: FlowStepFragment)

}
