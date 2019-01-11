package com.skyver.trybase.domain.interactor

import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.Repo
import javax.inject.Inject

class GetRepoesUC
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<List<Repo>, BaseUseCase.None>() {

    override suspend fun run(params: None) = reposRepository.repoes()
}
