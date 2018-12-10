package com.skyver.trybase.di.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.skyver.trybase.presentation.RepoesViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(RepoesViewModel::class)
    abstract fun bindsRepoesViewModel(repoesViewModel: RepoesViewModel): ViewModel
//
//    @Binds
//    @IntoMap
//    @ViewModelKey(MovieDetailsViewModel::class)
//    abstract fun bindsDetailsViewModel(detailsViewModel: DetailsViewModel): ViewModel
}