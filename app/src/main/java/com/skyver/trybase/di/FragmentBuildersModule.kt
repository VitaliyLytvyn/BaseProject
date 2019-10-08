package com.skyver.trybase.di


import com.skyver.trybase.presentation.AuthFragment
import com.skyver.trybase.presentation.FlowStepFragment
import com.skyver.trybase.presentation.FlowStepFragment2
import com.skyver.trybase.presentation.HomeFragment
import com.skyver.trybase.presentation.platform.BaseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Suppress("unused")
@Module
abstract class FragmentBuildersModule {
    @ContributesAndroidInjector
    abstract fun contributeHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributeBaseFragment(): BaseFragment

    @ContributesAndroidInjector
    abstract fun contributeAuthFragment(): AuthFragment

    @ContributesAndroidInjector
    abstract fun contributeFlowStepFragment(): FlowStepFragment

    @ContributesAndroidInjector
    abstract fun contributeFlowStepFragment2(): FlowStepFragment2
}
