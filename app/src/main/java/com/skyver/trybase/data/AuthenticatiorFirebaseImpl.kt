package com.skyver.trybase.data


import com.google.firebase.auth.FirebaseAuth
import com.skyver.trybase.domain.Authenticator
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthenticatiorFirebaseImpl
@Inject constructor() : Authenticator {

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun isLogedIn() = null != auth.currentUser

    //private val gitApi by lazy { retrofit.create(GitApi::class.java) }


}