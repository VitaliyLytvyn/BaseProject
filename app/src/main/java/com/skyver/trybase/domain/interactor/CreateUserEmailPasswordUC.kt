package com.skyver.trybase.domain.interactor

import com.skyver.trybase.domain.Authenticator
import javax.inject.Inject

class CreateUserEmailPasswordUC
@Inject constructor(private val authenticator: Authenticator) : BaseUseCase<Boolean, Pair<String, String>>() {

    override suspend fun run(params: Pair<String, String>) =
        authenticator.createUserEmailPassword(params.first, params.second)


    //todo test for error throw and delete!!!!
//    override suspend fun run(params: Pair<String, String>) : Either<Failure, AuthResult>{
//        if(true) throw Exception("My exception Authenticator create user!")
//        return authenticator.createUserEmailPassword(params.first, params.second)
//    }


}