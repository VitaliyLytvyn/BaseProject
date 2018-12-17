package com.skyver.trybase.presentation

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.skyver.trybase.domain.interactor.SignInSocialUC
import com.skyver.trybase.presentation.platform.BaseViewModel
import javax.inject.Inject

class AuthViewModel
@Inject constructor(
    private val signInSocialUC: SignInSocialUC
) : BaseViewModel() {

    var loginResult: MutableLiveData<Boolean> = MutableLiveData()

    fun signInWithSocial(credential: AuthCredential){
        signInSocialUC(uiScope, credential){it.either(::handleFailure, ::handleCreateUser) }
    }

    private fun handleCreateUser(authResult: Boolean) {
        this.loginResult.value = authResult
    }
}