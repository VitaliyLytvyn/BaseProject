package com.skyver.trybase.data

import com.google.android.gms.tasks.Task
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
    val usersReference = db.collection("users")//todo

    override suspend fun saveNewUser(user: UserAuthent): Either<Failure, Boolean> {
        return when (networkHandler.isConnected) {

            true -> taskToFirebaseSet((usersReference.document().set(user)), { true }, false)

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

//    override suspend fun getOneUserFromDB(key: String): Either<Failure, UserAuthent?> {
//        return when (networkHandler.isConnected) {
//            true -> {
//
//                val t = usersReference.document(key)
//                taskToFirebaseGet(t.get()) { document -> document.toObject(UserAuthent::class.java) }
//
//            }
//
//            false, null -> Either.Left(Failure.NetworkConnection())
//        }
//    }

//    override suspend fun getUsersFromDB(): Either<Failure, List<UserAuthent>> {
//        return when (networkHandler.isConnected) {
//            true -> {
//
//                //////////todo test
//                val t = usersReference.document("QCtLb1cj19uwbRZT8Qsl")
//
//                //taskToFirebaseGet(t.get()) { documents ->
//                getFromDbGeneric(t.get()) { documents ->
//
//                    e("QQQ enter documents for: ${documents}")
//                    val u = documents.toObject(UserAuthent::class.java)
//                    e("QQQ end documents for email: ${u?.email}")
//
//                }
//                //////////todo test
//
//                //taskToFirebaseGet(usersReference.get()) { documents ->
//                getFromDbGeneric(usersReference.get()) { documents ->
//                    documents.map { it.toObject(UserAuthent::class.java) }
//                }
//            }
//
//            false, null -> Either.Left(Failure.NetworkConnection())
//        }
//    }

    override suspend fun getOneUserFromDB(key: String): Either<Failure, UserAuthent?> {

        //val task = usersReference.document("QCtLb1cj19uwbRZT8Qsl").get()
        val task = usersReference.document(key).get()
        return getFromDbGeneric(task) { document -> document.toObject(UserAuthent::class.java) }
    }

    override suspend fun getUsersFromDB(): Either<Failure, List<UserAuthent>> {

        val task = usersReference.get()
        return getFromDbGeneric(task) { documents ->
            documents.map { it.toObject(UserAuthent::class.java) }
        }
    }

    private suspend fun <T, R> getFromDbGeneric(task: Task<T>, transform: (T) -> R): Either<Failure, R> {
        return when (networkHandler.isConnected) {
            true -> taskToFirebaseGet(task, transform)

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

    override fun repoes(): Either<Failure, List<Repo>> {
        return when (networkHandler.isConnected) {
            true -> request(service.repoes(), { it.map { it1 -> it1.toRepo() } }, emptyList())

            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

}