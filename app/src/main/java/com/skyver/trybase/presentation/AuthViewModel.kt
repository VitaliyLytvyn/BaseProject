package com.skyver.trybase.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthCredential
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.domain.interactor.CreateUserEmailPasswordUC
import com.skyver.trybase.domain.interactor.SaveUserUC
import com.skyver.trybase.domain.interactor.SignInSocialUC
import com.skyver.trybase.presentation.entity.UserEntity
import com.skyver.trybase.presentation.platform.BaseViewModel
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val signInSocialUC: SignInSocialUC,
    private val createUserEmailPasswordUC: CreateUserEmailPasswordUC,
    private val authenticator: Authenticator,
    private  val saveUserUC: SaveUserUC
    ) : BaseViewModel() {

    private var _loginResult: MutableLiveData<Boolean> = MutableLiveData()
    var loginResult: LiveData<Boolean> = _loginResult

    private var _saveResult: MutableLiveData<Boolean> = MutableLiveData()
    var saveResult: LiveData<Boolean> = _saveResult

    private var _userLiveData: MutableLiveData<UserEntity> = MutableLiveData()
    var userLiveData: LiveData<UserEntity> = _userLiveData

    init {
        authenticator.observeUser().observeForever { _userLiveData.value = it?.toUserEntity() }
    }

    fun signInWithSocial(credential: AuthCredential){
        signInSocialUC(viewModelScope, credential){it.either(::handleFailure, ::handleCreateUser) }
    }

    private fun handleCreateUser(authResult: Boolean) {
        this._loginResult.value = authResult
    }


    fun createUser(email: String, pass: String) =
        createUserEmailPasswordUC(viewModelScope, Pair(email, pass)) { it.either(::handleFailure, ::handleCreateUser) }

    fun saveUser(user: UserEntity) = saveUserUC(viewModelScope , user) { it.either(::handleFailure, ::handleSave) }

    private fun handleSave(b: Boolean) {
        this._saveResult.value = b
    }
}