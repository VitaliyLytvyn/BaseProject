package com.skyver.trybase.data

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Deferred
import retrofit2.Call
import timber.log.Timber
import timber.log.Timber.e


fun <T, R> request(call: Call<T>, transform: (T) -> R, default: T): Either<Failure, R> {
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
        Timber.e(exception)
        Either.Left(Failure.ServerError())
    }
}

suspend fun <T, R> taskToFirebaseSet(task: Task<T>, transform: (T) -> R, default: R): Either<Failure, R> {
    return try {
        val result = task.asDeferred().await()

        if (result != null) Either.Right(transform(result))//todo check for refactor
        else Either.Right(default)
    } catch (exception: Exception) {
        e(exception)//log exception
        Either.Left(Failure.ServerError(exception.message))
    }
}

//todo test this
suspend fun <T, R> taskToFirebaseGet(task: Task<T>, transform: (T) -> R): Either<Failure, R> {
    return try {
        task.asDeferred().await()

        var result = task.result

        //result of Task in Firebase can be both DocumentSnapshot and QuerySnapshot
        //for DocumentSnapshot(ONLY) absence can be as null or !res.exists() so need to check:
        if (result is DocumentSnapshot && !result.exists()) {
            result = null
        }

        if (result == null)
            Either.Left(Failure.NoSuchDocument())
        else
            Either.Right(transform(result))

    } catch (exception: Exception) {
        e(exception)//log exception
        Either.Left(Failure.ServerError(exception.message))
    }
}

fun <T> Task<T>.asDeferred(): Deferred<T> {
    val deferred = CompletableDeferred<T>()

    deferred.invokeOnCompletion {
        if (deferred.isCancelled) {
            // optional, handle coroutine cancellation however you'd like here
        }
    }

    this.addOnSuccessListener { result -> deferred.complete(result) }
    this.addOnFailureListener { exception -> deferred.completeExceptionally(exception) }

    return deferred
}