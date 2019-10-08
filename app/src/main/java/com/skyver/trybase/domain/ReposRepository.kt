package com.skyver.trybase.domain


import androidx.lifecycle.LiveData
import com.google.firebase.auth.AuthCredential
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.entity.UserAuthent
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure

interface ReposRepository {
    suspend fun repoes(): Either<Failure, List<Repo>>
//    fun repoDetails(movieId: Int): Either<Failure, RepoDetails>


    suspend fun saveNewUser(user: UserAuthent): Either<Failure, Boolean>
    suspend fun getUsersFromDB(): Either<Failure, List<UserAuthent>>
    suspend fun getOneUserFromDB(key: String): Either<Failure, UserAuthent?>
    suspend fun downloadFile(pathFrom: String): Either<Failure, LiveData<String>>
}
