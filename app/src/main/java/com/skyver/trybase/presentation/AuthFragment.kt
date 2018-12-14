package com.skyver.trybase.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.skyver.trybase.R
import com.skyver.trybase.presentation.platform.BaseFragment
import timber.log.Timber
import timber.log.Timber.e

class AuthFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        startAuthProcess()

        return inflater.inflate(R.layout.splash_layout, container, false)
    }

    private fun startAuthProcess() {

        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            if (resultCode == Activity.RESULT_OK) {

                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                e("Successfully signed in user: ${user?.displayName}")
                e("Successfully signed in providerId: ${user?.providerId}")
                e("Successfully signed in email: ${user?.email}")

                findNavController().navigate(
                    R.id.home_dest, null,
                    NavOptions.Builder()
                        .setPopUpTo(R.id.authFragment, true).build()
                )

            } else {
                notifyWithAction(R.string.failure_unknown_error, R.string.action_try_again, ::startAuthProcess)
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()

        //todo delete => for testing
        //authenticator.logOutUser()
    }

    companion object {
        const val RC_SIGN_IN = 47942
    }


}