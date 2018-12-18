package com.skyver.trybase.data

import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure
import com.skyver.trybase.presentation.platform.NetworkHandler
import retrofit2.Call
import timber.log.Timber.e
import javax.inject.Inject

class NetworkRepository
@Inject constructor(
    private val networkHandler: NetworkHandler,
    private val service: GitHubService
) : ReposRepository {

    override fun repoes(): Either<Failure, List<Repo>> {
        return when (networkHandler.isConnected) {
            true -> request(service.repoes(), { it.map { it.toRepo() } }, emptyList())
            false, null -> Either.Left(Failure.NetworkConnection())
        }
    }

//        override fun repoDetails(movieId: Int): Either<Failure, RepoDetails> {
//            return when (networkHandler.isConnected) {
//                true -> request(service.repoDetails(movieId), { it.toRepoDetails() }, RepoeDetailsEntity.empty())
//                false, null -> Left(NetworkConnection())
//            }
//        }

    private fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSuccessful) {
                true -> {
                    Either.Right(transform((response.body() ?: default)))
                }
                false -> Either.Left(
                    Failure.ServerError(
                        response.message()
                    )
                )
            }
        } catch (exception: Throwable) {
            e(exception)
            Either.Left(Failure.ServerError())
        }
    }

}