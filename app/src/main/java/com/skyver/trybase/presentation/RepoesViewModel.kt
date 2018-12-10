package com.skyver.trybase.presentation

import androidx.lifecycle.MutableLiveData
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.interactor.GetRepoes
import com.skyver.trybase.domain.interactor.UseCase
import com.skyver.trybase.presentation.platform.BaseViewModel
import com.skyver.trybase.presentation.entity.RepoView
import javax.inject.Inject

class RepoesViewModel
@Inject constructor(private val getRepoes: GetRepoes) : BaseViewModel() {

    var repoes: MutableLiveData<List<RepoView>> = MutableLiveData()

    fun loadMovies() = getRepoes(uiScope, UseCase.None()) { it.either(::handleFailure, ::handleMovieList) }

    private fun handleMovieList(movies: List<Repo>) {
        this.repoes.value = movies.map { RepoView(it.id, it.name, it.fullName, it.url) }
    }

}