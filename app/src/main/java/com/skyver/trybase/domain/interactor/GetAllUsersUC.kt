package com.skyver.trybase.domain.interactor

import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.UserAuthent
import javax.inject.Inject

class GetAllUsersUC
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<List<UserAuthent>, BaseUseCase.None>() {

    override suspend fun run(params: None) = reposRepository.getUsersFromDB()
}
