
package com.skyver.trybase.data

import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubService
@Inject constructor(retrofit: Retrofit) : GitApi {
    private val gitApi by lazy { retrofit.create(GitApi::class.java) }

    override suspend fun repoes() = gitApi.repoes()
    //override fun repoeDetails(repoId: Int) = gitApi.repoDetails(repoId)
}
