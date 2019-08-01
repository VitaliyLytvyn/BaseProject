package com.skyver.trybase.presentation.entity


data class UserEntity(
    var uid: String,
    var email: String?,
    var name: String?,
    var phoneNumber: String?,
    var photoUrl: String?
)