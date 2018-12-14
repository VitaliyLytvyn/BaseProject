package com.skyver.trybase.domain

import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthResult
import com.skyver.trybase.domain.entity.UserAuthent
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure

interface Authenticator{

    fun isLogedIn():Boolean

    fun logOutUser()

    fun observeUser(): LiveData<UserAuthent>

    suspend fun signInEmailPassword(email: String, password: String): Either<Failure, Boolean>

    suspend fun createUserEmailPassword(email: String, password: String): Either<Failure, Boolean>

//    suspend fun signInEmailPassword(email: String, password: String): Either<Failure, AuthResult>
//
//    suspend fun createUserEmailPassword(email: String, password: String): Either<Failure, AuthResult>

}