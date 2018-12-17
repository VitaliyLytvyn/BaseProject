package com.skyver.trybase.presentation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.skyver.trybase.R
import com.skyver.trybase.presentation.entity.RepoView
import com.skyver.trybase.presentation.extention.*
import com.skyver.trybase.presentation.platform.BaseFragment
import timber.log.Timber.e

class AuthFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel
    private var googleSignInClient: GoogleSignInClient? = null
    private var credential: AuthCredential? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        authViewModel = viewModel(viewModelFactory) {
            observe(loginResult, ::renderResult)
            failure(failure, ::handleFailure)
        }
    }

    private fun renderResult(success: Boolean?) {

        hideProgress()
        e("SUCCESS END!!! login with GOOGLE!!!!!!!!!!!!!!!!")

        //todo test
    }

    //todo
    private fun handleFailure(failure: Failure?) {
        hideProgress()

        when (failure) {
            is Failure.NetworkConnection -> renderFailure(fromResource(R.string.failure_network_connection))
            is Failure.ServerError -> {
                renderFailure(failure.cause ?: fromResource(R.string.failure_unknown_error))
            }

            is Failure.OtherError -> {
                renderFailure(failure.cause ?: fromResource(R.string.failure_unknown_error))
            }
        }
    }
    private fun renderFailure(message: String) {
        notifyWithAction(message, R.string.action_refresh, ::load)
    }
    fun load()={} //todo


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)

        googleSignInNonUI()
        //startAuthProcess()

        return inflater.inflate(R.layout.splash_layout, container, false)
    }

    //UI library
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

    //NON UI library
    private fun googleSignInNonUI() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            //.requestIdToken(getString(R.string.default_web_client_id))
            .requestIdToken("943599322346-nbn0q7gbv4uj56e7tc1om28dct5b9pgr.apps.googleusercontent.com")
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)


        signInGoogle()
    }


    private fun signInGoogle() {
        googleSignInClient?.let {
            val signInIntent = it.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                e("Google sign in SUCCESS!! email: ${account?.email}")
                //firebaseAuthWithGoogle(account!!)

                credential = GoogleAuthProvider.getCredential(account!!.idToken, null)


                //todo
                showProgress()
                authViewModel.signInWithSocial(credential!!)//todo null!!
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                e("Google sign in failed: ${e.message}")
                // ...
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
        const val RC_GOOGLE_SIGN_IN = 47943
    }


}