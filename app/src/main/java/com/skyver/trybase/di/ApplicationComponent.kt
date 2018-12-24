package com.skyver.trybase.di

import com.skyver.trybase.AppClass
import com.skyver.trybase.di.viewmodel.ViewModelModule
import com.skyver.trybase.presentation.AuthFragment
import com.skyver.trybase.presentation.FlowStepFragment
import com.skyver.trybase.presentation.FlowStepFragment2
import com.skyver.trybase.presentation.HomeFragment
import com.skyver.trybase.presentation.platform.BaseFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, ViewModelModule::class])
interface ApplicationComponent {
    fun inject(application: AppClass)
    fun inject(flowStepFragment: FlowStepFragment)
    fun inject(baseFragment: BaseFragment)
    fun inject(authFragment: AuthFragment)
    fun inject(flowStepFragment2: FlowStepFragment2)
    fun inject(homeFragment: HomeFragment)

}
