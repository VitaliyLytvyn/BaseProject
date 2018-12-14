package com.skyver.trybase.domain.interactor

import com.google.firebase.auth.AuthResult
import com.skyver.trybase.domain.Authenticator
import com.skyver.trybase.presentation.extention.Failure
import javax.inject.Inject

class CreateUserEmailPasswordUC
@Inject constructor(private val authenticator: Authenticator) : BaseUseCase<Boolean, Pair<String, String>>() {
//@Inject constructor(private val authenticator: Authenticator) : BaseUseCase<AuthResult, Pair<String, String>>() {

    override suspend fun run(params: Pair<String, String>) =
        authenticator.createUserEmailPassword(params.first, params.second)


    //todo test for error throw and delete!!!!
//    override suspend fun run(params: Pair<String, String>) : Either<Failure, AuthResult>{
//        if(true) throw Exception("My exception Authenticator create user!")
//        return authenticator.createUserEmailPassword(params.first, params.second)
//    }


}