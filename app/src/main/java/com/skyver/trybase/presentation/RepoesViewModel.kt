package com.skyver.trybase.presentation

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.interactor.GetRepoesUseCase
import com.skyver.trybase.domain.interactor.BaseUseCase
import com.skyver.trybase.domain.interactor.CreateUserEmailPasswordUC
import com.skyver.trybase.presentation.platform.BaseViewModel
import com.skyver.trybase.presentation.entity.RepoView
import com.skyver.trybase.presentation.entity.User
import javax.inject.Inject

class RepoesViewModel
@Inject constructor(
    private val getRepoesUseCase: GetRepoesUseCase,
    private val createUserEmailPasswordUC: CreateUserEmailPasswordUC,
    private val authenticator: Authenticator
) : BaseViewModel() {

    var loginResult: MutableLiveData<Boolean> = MutableLiveData()
    var repoes: MutableLiveData<List<RepoView>> = MutableLiveData()
    var userLiveData: MutableLiveData<User> = MutableLiveData()

    init {
        authenticator.observeUser().observeForever { userLiveData.value = it.toUser() }
    }

    fun loadRepoes() = getRepoesUseCase(uiScope, BaseUseCase.None()) { it.either(::handleFailure, ::handlerepoList) }

    private fun handlerepoList(repoes: List<Repo>) {
        this.repoes.value = repoes.map { RepoView(it.id, it.name, it.fullName, it.url) }
    }

    fun craeteUser(email: String, pass: String) =
        createUserEmailPasswordUC(uiScope, Pair(email, pass)) { it.either(::handleFailure, ::handleCreateUser) }

    private fun handleCreateUser(authResult: Boolean) {
        this.loginResult.value = authResult
    }
}