package com.skyver.trybase.data

import com.google.firebase.firestore.FirebaseFirestore
import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.entity.UserAuthent
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure
import com.skyver.trybase.presentation.platform.NetworkHandler
import javax.inject.Inject

class NetworkRepository
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: GitHubService
) : ReposRepository {

    // Access a Cloud Firestore instance from your Activity
    val db = FirebaseFirestore.getInstance()//todo

    override suspend fun saveNewUser(user: UserAuthent): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {

            true -> taskToFirebase((db.collection("users").document().set(user)), { true}, false)

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override fun repoes(): Either<Failure, List<Repo>> {
        return when (networkHandler.isConnected) {
            true -> request(service.repoes(), { it.map { it.toRepo() } }, emptyList())
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }


}