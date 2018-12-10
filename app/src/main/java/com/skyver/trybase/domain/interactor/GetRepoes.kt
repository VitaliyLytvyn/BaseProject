package com.skyver.trybase.domain.interactor


import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.Repo
import com.skyver.trybase.presentation.extention.Failure
import java.lang.Exception
import javax.inject.Inject

class GetRepoes
@Inject constructor(private val reposRepository: ReposRepository) : UseCase<List<Repo>, UseCase.None>() {

    override suspend fun run(params: None) = reposRepository.repoes()


    //todo test for error throw and delete!!!!
//    override suspend fun run(params: None): Either<Failure, List<Repo>> {
//        if(true) throw Exception("My test exception TWO!")
//        return reposRepository.repoes()
//    }

}
