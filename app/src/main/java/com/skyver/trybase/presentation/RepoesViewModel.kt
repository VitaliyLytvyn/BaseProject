package com.skyver.trybase.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.entity.UserAuthent
import com.skyver.trybase.domain.interactor.GetRepoesUC
import com.skyver.trybase.domain.interactor.BaseUseCase
import com.skyver.trybase.domain.interactor.GetAllUsersUC
import com.skyver.trybase.presentation.platform.BaseViewModel
import com.skyver.trybase.presentation.entity.RepoView
import com.skyver.trybase.presentation.entity.UserEntity
import javax.inject.Inject

class RepoesViewModel
@Inject constructor(
    private val getRepoesUseCase: GetRepoesUC,
    private val getAllUsersUC: GetAllUsersUC
) : BaseViewModel() {

    private var _repoes: MutableLiveData<List<RepoView>> = MutableLiveData()
    var repoes: LiveData<List<RepoView>> = _repoes

    private var _allUsers: MutableLiveData<List<UserEntity>> = MutableLiveData()
    var allUsers: LiveData<List<UserEntity>> = _allUsers


    fun loadRepoes() = getRepoesUseCase(uiScope, BaseUseCase.None()) { it.either(::handleFailure, ::handlerepoList) }

    private fun handlerepoList(repoes: List<Repo>) {
        this._repoes.value = repoes.map { RepoView(it.id, it.name, it.fullName, it.url) }
    }


    fun loadAllUsers() = getAllUsersUC(uiScope, BaseUseCase.None()) { it.either(::handleFailure, ::handleAllUsersList) }

    private fun handleAllUsersList(repoes: List<UserAuthent>) {
        this._allUsers.value = repoes.map { UserEntity(it.uid, it.email, it.name,  it.phoneNumber, it.photoUrl) }
    }

}