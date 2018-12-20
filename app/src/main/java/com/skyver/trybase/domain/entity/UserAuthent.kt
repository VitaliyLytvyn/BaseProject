package com.skyver.trybase.domain.entity

import android.net.Uri
import com.google.firebase.firestore.Exclude
import com.skyver.trybase.presentation.entity.UserEntity

data class UserAuthent(
    val uid: String,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val photoUrl: String?
){
    @Exclude
    fun toUserEntity() = UserEntity(uid, email, name, phoneNumber, photoUrl)

    constructor(ent: UserEntity):this( ent.uid, ent.email, ent.name, ent.phoneNumber, ent.photoUrl)
}
