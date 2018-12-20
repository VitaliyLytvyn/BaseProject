package com.skyver.trybase.domain.interactor

import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.UserAuthent
import com.skyver.trybase.presentation.entity.UserEntity
import javax.inject.Inject

class SaveUserUC

@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<Boolean, UserEntity>() {

    override suspend fun run(params: UserEntity) =
        reposRepository.saveNewUser(UserAuthent(params))


    //todo test for error throw and delete!!!!
//    override suspend fun run(params: Pair<String, String>) : Either<Failure, AuthResult>{
//        if(true) throw Exception("My exception Authenticator create user!")
//        return authenticator.createUserEmailPassword(params.first, params.second)
//    }


}