package com.skyver.trybase.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.domain.interactor.CreateUserEmailPasswordUC
import com.skyver.trybase.domain.interactor.SignInSocialUC
import com.skyver.trybase.presentation.entity.User
import com.skyver.trybase.presentation.platform.BaseViewModel
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val signInSocialUC: SignInSocialUC,
    private val createUserEmailPasswordUC: CreateUserEmailPasswordUC,
    private val authenticator: Authenticator
    ) : BaseViewModel() {

    var loginResult: MutableLiveData<Boolean> = MutableLiveData()

    private var _userLiveData: MutableLiveData<User> = MutableLiveData()
    var userLiveData: LiveData<User> = _userLiveData

    init {
        authenticator.observeUser().observeForever { _userLiveData.value = it?.toUser() }
    }

    fun signInWithSocial(credential: AuthCredential){
        signInSocialUC(uiScope, credential){it.either(::handleFailure, ::handleCreateUser) }
    }

    private fun handleCreateUser(authResult: Boolean) {
        this.loginResult.value = authResult
    }


    fun craeteUser(email: String, pass: String) =
        createUserEmailPasswordUC(uiScope, Pair(email, pass)) { it.either(::handleFailure, ::handleCreateUser) }

}