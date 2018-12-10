package com.skyver.trybase.data

import com.google.gson.annotations.SerializedName
import com.skyver.trybase.domain.entity.Repo

data class GitRepo(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("full_name")
    val fullName: String,
    @SerializedName("url")
    val url: String
) {
    fun toRepo() = Repo(id, name, fullName, url)
}

