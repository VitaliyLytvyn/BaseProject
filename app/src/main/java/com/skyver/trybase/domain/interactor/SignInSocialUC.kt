package com.skyver.trybase.domain.interactor

import com.google.firebase.auth.AuthCredential
import com.skyver.trybase.domain.Authenticator
import javax.inject.Inject

class SignInSocialUC
@Inject constructor(private val authenticator: Authenticator) : BaseUseCase<Boolean, AuthCredential>() {

    override suspend fun run(params: AuthCredential) =
        authenticator.signInUserSocial(params)


    //todo test for error throw and delete!!!!
//    override suspend fun run(params: Pair<String, String>) : Either<Failure, AuthResult>{
//        if(true) throw Exception("My exception Authenticator create user!")
//        return authenticator.createUserEmailPassword(params.first, params.second)
//    }


}