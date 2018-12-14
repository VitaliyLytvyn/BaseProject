package com.skyver.trybase.domain.interactor

import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.Repo
import javax.inject.Inject

class GetRepoesUseCase
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<List<Repo>, BaseUseCase.None>() {

    override suspend fun run(params: None) = reposRepository.repoes()


    //todo test for error throw and delete!!!!
//    override suspend fun run(params: None): Either<Failure, List<Repo>> {
//        if(true) throw Exception("My test exception TWO!")
//        return reposRepository.repoes()
//    }

}
