package com.skyver.trybase.domain.interactor


import androidx.lifecycle.LiveData
import com.skyver.trybase.domain.ReposRepository
import com.skyver.trybase.domain.entity.UserAuthent
import javax.inject.Inject

class DownloadLargeFileUC
@Inject constructor(private val reposRepository: ReposRepository) : BaseUseCase<LiveData<String>, String>() {

    override suspend fun run(params: String) = reposRepository.downloadFile(params)
}
