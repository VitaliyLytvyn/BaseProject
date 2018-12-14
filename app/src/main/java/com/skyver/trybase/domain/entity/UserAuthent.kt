package com.skyver.trybase.domain.entity

import android.net.Uri
import com.skyver.trybase.presentation.entity.User

data class UserAuthent(
    val uid: String,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val photoUrl: Uri?
){
    fun toUser() = User(uid, email, name, phoneNumber, photoUrl)
}
