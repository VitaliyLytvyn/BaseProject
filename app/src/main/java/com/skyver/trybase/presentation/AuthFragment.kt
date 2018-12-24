package com.skyver.trybase.presentation

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.skyver.trybase.R
import com.skyver.trybase.presentation.extention.*
import com.skyver.trybase.presentation.platform.BaseFragment
import timber.log.Timber.e
import com.google.android.gms.common.SignInButton
import com.skyver.trybase.presentation.entity.UserEntity
import kotlinx.android.synthetic.main.auth_fragment_layout.*
import pub.devrel.easypermissions.AfterPermissionGranted
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import timber.log.Timber.d


class AuthFragment : BaseFragment() {

    private lateinit var authViewModel: AuthViewModel
    private var googleSignInClient: GoogleSignInClient? = null
    private var credential: AuthCredential? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.inject(this)

        setUpVieModel()
    }

    private fun setUpVieModel() {
        authViewModel = viewModel(viewModelFactory) {
            observe(userLiveData, ::renderUserChange)
            observe(loginResult, ::renderLoginSuccess)
            failure(failure, ::handleFailure)


            observe(saveResult, ::renderSaveSuccess)//todo
        }
    }

    private fun renderSaveSuccess(result: Boolean?) {
        hideProgress()
        if (result != null && result == true) {
            notify(R.string.logged_in)
        }

        goInAndDisableReturn()
    }

    private fun renderUserChange(user: UserEntity?) {
        val url = user?.photoUrl
        if (url != null) imageViewAvatar.loadFromUrl(url.toString())
        else imageViewAvatar.setImageResource(R.drawable.ic_android)

        textViewName.text = user?.name ?: ""
        textViewEmail.text = user?.email ?: ""


        //todo
        user?.let { authViewModel.saveUser(it) }
    }

    private fun renderLoginSuccess(authResult: Boolean?) {
        hideProgress()
        if (authResult != null && authResult == true) {
            notify(R.string.logged_in)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.auth_fragment_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setUpGoogleSignInNonUI()
        //startAuthUiProcess()
    }

    //UI library todo consider choose either firebase-auth or firebase-ui-auth only
    private fun startAuthUiProcess() {
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.TwitterBuilder().build(),
            AuthUI.IdpConfig.FacebookBuilder().build()
        )

        // Create and launch sign-in intent
        startActivityForResult(
            AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(),
            RC_UI_LIBRARY_SIGN_IN
        )
    }

    //NON UI library
    private fun setUpGoogleSignInNonUI() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id_2))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(activity!!, gso)

        // Set the dimensions of the sign-in button.
        //val signInButton = findViewById(R.id.sign_in_button)
        google_sign_in_button?.setSize(SignInButton.SIZE_STANDARD)
        google_sign_in_button?.setOnClickListener {

            //signInGoogle()
            locationAndContactsTask()//todo delete / change
        }

    }

    private fun signInGoogle() {
        googleSignInClient?.let {
            val signInIntent = it.signInIntent
            startActivityForResult(signInIntent, RC_GOOGLE_SIGN_IN)
        }

    }

    //NON UI library Email/Password flow todo implement
    private fun createEmailPasswordUser(email: String, password: String) {
        //authViewModel.createUser(email, password)
        authViewModel.createUser("meTEST@hg.com", "free123445")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_UI_LIBRARY_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {

                // Successfully signed in and authenticated with UI library
                goInAndDisableReturn()

            } else {
                notifyWithAction(R.string.failure_unknown_error, R.string.action_try_again, ::startAuthUiProcess)
            }
        }

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        else if (requestCode == RC_GOOGLE_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)

                    account ?: return

                    credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    credential?.let {
                        showProgress()
                        authViewModel.signInWithSocial(it)
                    }

                } catch (exc: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    e(exc)
                    notify(exc.localizedMessage)
                }
            }

        }

        //todo for Runtime permission - do or do NOT something after return from settings
        else if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            e("after settings OK -> locationAndContactsTask()")
            if (hasLocationAndContactsPermissions()) {
                // Have permissions, do the thing!
                e("have permission")
                Toast.makeText(activity, "TODO: Location and Contacts things", Toast.LENGTH_LONG).show()
            }
        }

    }

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
        notify(message)
    }

    private fun goInAndDisableReturn() {
        findNavController().navigate(
            R.id.home_dest, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.authFragment, true).build() //prevents return here on back button press
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()

        //todo delete => for testing
        authenticator.logOutUser()
    }

    companion object {
        const val RC_UI_LIBRARY_SIGN_IN = 47942
        const val RC_GOOGLE_SIGN_IN = 47943

        const val RC_LOCATION_PERM = 449
    }

    //todo for Runtime permission
    @AfterPermissionGranted(RC_LOCATION_PERM)
    fun locationAndContactsTask() {
        if (hasLocationAndContactsPermissions()) {
            // Have permissions, do the thing!
            d("have permission")
        } else {
            e("have NO permission")
            // Ask for both permissions
            EasyPermissions.requestPermissions(
                this,
                "rationale for location",
                RC_LOCATION_PERM,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    //todo for Runtime permission
    private fun hasLocationAndContactsPermissions(): Boolean {
        return EasyPermissions.hasPermissions(activity!!, Manifest.permission.ACCESS_FINE_LOCATION)
    }

}