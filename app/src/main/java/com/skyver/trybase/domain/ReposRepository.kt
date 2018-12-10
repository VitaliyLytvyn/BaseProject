package com.skyver.trybase.domain


import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.domain.interactor.Either
import com.skyver.trybase.presentation.extention.Failure

interface ReposRepository {
    fun repoes(): Either<Failure, List<Repo>>
//    fun repoDetails(movieId: Int): Either<Failure, RepoDetails>

}
