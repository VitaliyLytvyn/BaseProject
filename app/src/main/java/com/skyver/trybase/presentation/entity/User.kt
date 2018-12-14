package com.skyver.trybase.presentation.entity

import android.net.Uri

data class User(
    val uid: String,
    val email: String?,
    val name: String?,
    val phoneNumber: String?,
    val photoUrl: Uri?
)