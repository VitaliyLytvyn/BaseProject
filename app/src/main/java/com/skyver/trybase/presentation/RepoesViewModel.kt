package com.skyver.trybase.presentation

import androidx.lifecycle.MutableLiveData
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.interactor.GetRepoesUseCase
import com.skyver.trybase.domain.interactor.BaseUseCase
import com.skyver.trybase.presentation.platform.BaseViewModel
import com.skyver.trybase.presentation.entity.RepoView
import javax.inject.Inject

class RepoesViewModel
@Inject constructor(
    private val getRepoesUseCase: GetRepoesUseCase
) : BaseViewModel() {

    var repoes: MutableLiveData<List<RepoView>> = MutableLiveData()

    fun loadRepoes() = getRepoesUseCase(uiScope, BaseUseCase.None()) { it.either(::handleFailure, ::handlerepoList) }

    private fun handlerepoList(repoes: List<Repo>) {
        this.repoes.value = repoes.map { RepoView(it.id, it.name, it.fullName, it.url) }
    }

}