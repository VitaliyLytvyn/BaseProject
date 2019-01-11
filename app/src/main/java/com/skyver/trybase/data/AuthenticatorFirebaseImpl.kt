package com.skyver.trybase.data


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.skyver.trybase.data.mapper.UserMapper
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.domain.entity.UserAuthent
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure
import com.skyver.trybase.presentation.platform.NetworkHandler
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthenticatorFirebaseImpl
@Inject constructor(val context: Context, private val networkHandler: NetworkHandler) : Authenticator {

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()

    private var _currentUserLD: MutableLiveData<UserAuthent> = MutableLiveData()
    var currentUser: LiveData<UserAuthent> = _currentUserLD

    init {
        auth.addAuthStateListener { _currentUserLD.value = UserMapper.toUserAuth(auth.currentUser) }
    }

    override fun observeUser(): LiveData<UserAuthent> = currentUser
    override fun isLogedIn() = null != auth.currentUser


    override suspend fun createUserEmailPassword(email: String, password: String): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {
            true -> {
                taskToFirebaseSet(auth.createUserWithEmailAndPassword(email, password), { true }, false)
            }

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override suspend fun signInUserSocial(credential: AuthCredential): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {
            true -> taskToFirebaseSet(auth.signInWithCredential(credential), { true }, false)

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override suspend fun signInEmailPassword(email: String, password: String): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {
            true -> {
                taskToFirebaseSet(auth.signInWithEmailAndPassword(email, password), { true }, false)
            }

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override fun logOutUser() {
        AuthUI.getInstance()
            .signOut(context)
    }

}