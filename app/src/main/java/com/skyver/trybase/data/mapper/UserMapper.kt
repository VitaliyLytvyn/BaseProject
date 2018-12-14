package com.skyver.trybase.data.mapper

import com.google.firebase.auth.FirebaseUser
import com.skyver.trybase.domain.entity.UserAuthent

class UserMapper {

    companion object {
        fun toUserAuth(u: FirebaseUser?) =
            if (null == u) u
            else {
                UserAuthent(
                    uid = u.uid,
                    email = u.email,
                    name = u.displayName,
                    phoneNumber = u.phoneNumber,
                    photoUrl = u.photoUrl
                )
            }
    }
}