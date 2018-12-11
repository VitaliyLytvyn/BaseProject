package com.skyver.trybase.data


import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.skyver.trybase.domain.Authenticator
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AuthenticatorFirebaseImpl
@Inject constructor(val context: Context) : Authenticator {
    override fun logOutUser() {
        AuthUI.getInstance()
            .signOut(context)
    }

    // Initialize Firebase Auth
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()


    override fun isLogedIn() = null != auth.currentUser

    //private val gitApi by lazy { retrofit.create(GitApi::class.java) }


}