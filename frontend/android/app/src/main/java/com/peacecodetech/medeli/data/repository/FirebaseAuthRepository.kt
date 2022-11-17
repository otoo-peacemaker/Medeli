package com.peacecodetech.medeli.data.repository

import android.app.Activity
import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.model.User
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.Resource
import dagger.hilt.android.qualifiers.ActivityContext
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository  @Inject constructor(
    private val auth: FirebaseAuth,
    //private var signInClient: SignInClient,
    private val fireStore: FirebaseFirestore
) : BaseFragment() {

    /**
     * A variable for current user
     * */
    private val currentUser = auth.currentUser




    /***
     * Observe user live data after login
     * */
    private val userLiveData = MutableLiveData<Resource<User>?>()

    /**
     * This variable is used to launch google sign in client
     * */
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
          //  handleGoogleSignInResult(result.data)
        }

    init {
        // Configure Google Sign In
        // signInClient = Identity.getSignInClient(context)
    }


    /**
     * A function to sign up user in our database
     * @param isEnable enable or disable your UI buttons
     * @param email user email
     * @param password user password
     * @sample updateUI
     * */
    fun signUpWithEmail(
        isEnable: Button? = null,
        email: String,
        password: String)= auth.createUserWithEmailAndPassword(email, password)



    /**
     * @param isEnable enable or disable your UI buttons
     * */

    fun sendEmailVerification(isEnable: Button? = null) = currentUser?.sendEmailVerification()

    fun forgotPassword()=currentUser


    /**
     * A function to sign user in our database
     * @param email user email
     * @param password user password
     * @param isEnable enable or disable your UI buttons
     * @sample updateUI
     * */

    fun signInExistingUserWithEmail(
        isEnable: Boolean? = null,
        email: String,
        password: String
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(TAG).d("signInWithEmail:success")
                    //   _updateUserUI.postValue(currentUser)
                    userLiveData.postValue(
                        Resource.success(
                            User(
                                id = currentUser?.uid,
                                email = currentUser?.email,
                                fullName = currentUser?.displayName,
                                password = currentUser?.phoneNumber
                            )
                        )
                    )

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag(TAG).w(task.exception, "signInWithEmail:failure")
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    userLiveData.postValue(
                        Resource.error(
                            null,
                            message = task.exception?.message.toString()
                        )
                    )
                }
            }
    }

/*
    *//**
     * Sign in with google using the [firebaseAuthWithGoogle]
     * @param webClientId your client id from google console
     * *//*
    fun signInWithGoogle(webClientId: String) {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(webClientId)
            .build()
        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchGoogleSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Timber.tag(TAG).e(e, getString(R.string.goggle_sign_in_failed))
            }

    }*/


    /**
     * Display One-Tap Sign In with google if user isn't logged in
     * Call this inside [onViewCreated] method
     * */
    /*
    fun oneTapSignInWithGoogle(webClientId: String) {
        if (currentUser == null) {
            // Configure One Tap UI
            val oneTapRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                    BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(webClientId)
                        .setFilterByAuthorizedAccounts(true)
                        .build()
                )
                .build()

            // Display the One Tap UI
            signInClient.beginSignIn(oneTapRequest)
                .addOnSuccessListener { result ->
                    launchGoogleSignIn(result.pendingIntent)
                }
                .addOnFailureListener { e ->
                    Timber.tag("OnTap").d(e)
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                }
        }
    }*/

    /**
     *  Result returned from launching the Sign In PendingIntent
     * */

    /*private fun handleGoogleSignInResult(
        data: Intent?
    ) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Timber.tag(TAG).d("firebaseAuthWithGoogle: %s", credential.id)
                firebaseAuthWithGoogle(idToken)
            } else {
                // Shouldn't happen.
                Timber.tag(TAG).d(getString(R.string.no_id_token))
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Timber.tag(TAG).w(e, "Google sign in failed")
            userLiveData.postValue(null)
        }
    }*/

    private fun firebaseAuthWithGoogle(
        idToken: String) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(TAG).d(getString(R.string.sign_with_credential))
                    //   this._updateUserUI.postValue(currentUser)
                    userLiveData.postValue(
                        Resource.success(
                            User(
                                id = currentUser?.uid,
                                email = currentUser?.email,
                                fullName = currentUser?.displayName,
                                password = currentUser?.phoneNumber
                            )
                        )
                    )
                    //Timber.tag(TAG).d("",task.result.user)

                } else {
                    // If sign in fails, display a message to the user.
                    Timber.tag(TAG)
                        .w(task.exception, getString(R.string.sign_with_credential_failure))
                    Snackbar.make(
                        requireView(),
                        getString(R.string.auth_failed),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    this.userLiveData.postValue(null)
                }

                hideProgressBar()
            }
    }


    private fun launchGoogleSignIn(pendingIntent: PendingIntent) {
        try {
            val intentSenderRequest = IntentSenderRequest.Builder(pendingIntent)
                .build()
            signInLauncher.launch(intentSenderRequest)
        } catch (e: IntentSender.SendIntentException) {
            Timber.tag(TAG).e("Couldn't start Sign In: %s", e.localizedMessage)
        }
    }


    /**
     * A function to sign user out from the database
     * In this sign out function we pass the [currentUser]
     * @sample updateUI
     * */

    fun signOut() {
        auth.signOut()
        userLiveData.postValue(null)

        // Google sign out
       /* signInClient.signOut().addOnCompleteListener(requireActivity()) {
            userLiveData.postValue(null)
        }*/
    }

    fun saveUser(fullName:String, email: String, password: String) =
        fireStore.collection("users").document(email).set(
            User(
                uid = currentUser?.uid,
                fullName = fullName,
                email = email,
                password = password
            )
        )


    /**
     * Check if user is signed in (non-null) and update UI accordingly.
     *
     * call this method in the [onStart()]
     * @sample updateUI
     * */

    fun reloadUser() {
        if (currentUser != null) {
            auth.currentUser!!.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userLiveData.postValue(
                        Resource.success(
                            User(
                                id = currentUser.uid,
                                email = currentUser.email,
                                fullName = currentUser.displayName,
                                password = currentUser.phoneNumber
                            )
                        )
                    )
                    Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Timber.tag(TAG).e(task.exception, "reload")
                    Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun getUserLiveData() = userLiveData

    /**
     * If a user has signed in successfully you can get their account data at any point with the getCurrentUser method.
     * To get the profile information retrieved from the sign-in providers linked to a user, use the [getProviderData] method.
     * @sample getUserPnPInfo
     * */

    fun accessUserProfileAndProvidedInfo(userInformation: (user: FirebaseUser?) -> Unit) =
        userInformation(currentUser)


    /** update user example**/
    private fun updateUI(user: FirebaseUser?) {
        /* hideProgressBar()
         if (user != null) {
             binding.status.text = getString(R.string.emailpassword_status_fmt,
                 user.email, user.isEmailVerified)
             binding.detail.text = getString(R.string.firebase_status_fmt, user.uid)

             binding.emailPasswordButtons.visibility = View.GONE
             binding.emailPasswordFields.visibility = View.GONE
             binding.signedInButtons.visibility = View.VISIBLE

             if (user.isEmailVerified) {
                 binding.verifyEmailButton.visibility = View.GONE
             } else {
                 binding.verifyEmailButton.visibility = View.VISIBLE
             }
         } else {
             binding.status.setText(R.string.signed_out)
             binding.detail.text = null

             binding.emailPasswordButtons.visibility = View.VISIBLE
             binding.emailPasswordFields.visibility = View.VISIBLE
             binding.signedInButtons.visibility = View.GONE
         }*/
    }

    private fun getUserPnPInfo() {
        currentUser?.let {
            for (profile in it.providerData) {
                // Id of the provider (ex: google.com)
                val providerId = profile.providerId

                // UID specific to the provider
                val uid = profile.uid

                // Name, email address, and profile photo Url
                val name = profile.displayName
                val email = profile.email
                val photoUrl = profile.photoUrl
            }
        }
    }


    // private val updateUserUI: (user: MutableLiveData<FirebaseUser?>) -> Unit
}