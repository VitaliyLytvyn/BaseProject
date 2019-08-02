package com.skyver.trybase.data

import retrofit2.Call
import retrofit2.http.GET

internal interface GitApi {
    companion object {
        private const val REPOSITORIES = "users/VitaliyLytvyn/repos"
    }

    @GET(REPOSITORIES) suspend fun repoes(): List<GitRepo>
    //@GET(REPOSITORIES) fun repoes(): Call<List<GitRepo>>
    //@GET(REPO_DETAILS) fun repoDetails(@Path(PARAM_REPO_ID) repoId: Int): Call<RepoDetailsEntity>
}
