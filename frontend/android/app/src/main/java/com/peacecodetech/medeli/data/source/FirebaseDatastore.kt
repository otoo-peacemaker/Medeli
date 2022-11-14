package com.peacecodetech.medeli.data.source

import android.app.PendingIntent
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.GetSignInIntentRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.util.BaseFragment

class FirebaseDatastore(
    private val auth: FirebaseAuth,
    private var signInClient: SignInClient
) : BaseFragment() {

    /**
     * A variable for current user
     * */
    private val currentUser = auth.currentUser

    /**
     * This variable is used to launch google sign in client
     * */
    private val signInLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            handleGoogleSignInResult(result.data) { currentUser }
        }

    init {
        // Configure Google Sign In
        signInClient = Identity.getSignInClient(requireContext())
    }


    /**
     * A function to sign up user in our database
     * @param isEnable enable or disable your UI buttons
     * @param email user email
     * @param password user password
     * @param updateUserUI is any generic lambda function that user update ui. Create a function that accept [FirebaseUser] and implement your UI logic.
     * @sample updateUI
     * */
    fun signUpWithEmail(
        isEnable: Button? = null,
        email: String,
        password: String,
        updateUserUI: (user: FirebaseUser?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    updateUserUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                    updateUserUI(null)
                }
            }
    }


    /**
     * @param isEnable enable or disable your UI buttons
     * */

    fun sendEmailVerification(isEnable: Button? = null) {
        // Disable button
        isEnable?.isEnabled = false
        // Send verification email
        currentUser?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                // Re-enable button
                isEnable?.isEnabled = true
                if (task.isSuccessful) {
                    Toast.makeText(
                        context,
                        "Verification email sent to ${currentUser.email} ",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        context,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }


    /**
     * A function to sign user in our database
     * @param email user email
     * @param password user password
     * @param isEnable enable or disable your UI buttons
     * @param updateUserUI is any generic lambda function that user update ui. Create a function that accept [FirebaseUser] and implement your UI logic.
     * @sample updateUI
     * */

    fun signInExistingUserWithEmail(
        isEnable: Boolean? = null,
        email: String,
        password: String,
        updateUserUI: (user: FirebaseUser?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    updateUserUI(currentUser)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(requireContext(), "Authentication failed.", Toast.LENGTH_SHORT)
                        .show()
                    updateUserUI(null)
                }
            }
    }


    /**
     * Sign in with google using the [firebaseAuthWithGoogle]
     * @param webClientId your client id from google console
     * */
    fun signInWithGoogle(webClientId: String) {
        val signInRequest = GetSignInIntentRequest.builder()
            .setServerClientId(webClientId)
            .build()
        signInClient.getSignInIntent(signInRequest)
            .addOnSuccessListener { pendingIntent ->
                launchGoogleSignIn(pendingIntent)
            }
            .addOnFailureListener { e ->
                Log.e(TAG, getString(R.string.goggle_sign_in_failed), e)
            }

    }


    /**
     * Display One-Tap Sign In with google if user isn't logged in
     * Call this inside [onViewCreated] method
     * */
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
                    Log.d("OnTap", "$e")
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                }
        }
    }

    /**
     *  Result returned from launching the Sign In PendingIntent
     * */

    private fun handleGoogleSignInResult(
        data: Intent?,
        updateUserUI: (user: FirebaseUser?) -> Unit
    ) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            val credential = signInClient.getSignInCredentialFromIntent(data)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Log.d(TAG, "firebaseAuthWithGoogle: ${credential.id}")
                firebaseAuthWithGoogle(idToken, updateUserUI)
            } else {
                // Shouldn't happen.
                Log.d(TAG, getString(R.string.no_id_token))
            }
        } catch (e: ApiException) {
            // Google Sign In failed, update UI appropriately
            Log.w(TAG, "Google sign in failed", e)
            updateUserUI(null)
        }
    }

    private fun firebaseAuthWithGoogle(
        idToken: String,
        updateUserUI: (user: FirebaseUser?) -> Unit
    ) {
        showProgressBar()
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, getString(R.string.sign_with_credential))
                    updateUserUI(currentUser)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, getString(R.string.sign_with_credential_failure), task.exception)
                    Snackbar.make(
                        requireView(),
                        getString(R.string.auth_failed),
                        Snackbar.LENGTH_SHORT
                    ).show()
                    updateUserUI(null)
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
            Log.e(TAG, "Couldn't start Sign In: ${e.localizedMessage}")
        }
    }


    /**
     * A function to sign user out from the database
     * @param updateUserUI is any generic lambda function that user update ui. Create a function that accept [FirebaseUser] and implement your UI logic.
     * @sample updateUI
     * */

    fun signOut(updateUserUI: (user: FirebaseUser?) -> Unit) {
        auth.signOut()
        updateUserUI(null)

        // Google sign out
        signInClient.signOut().addOnCompleteListener(requireActivity()) {
            updateUserUI(null)
        }
    }


    /**
     * Check if user is signed in (non-null) and update UI accordingly.
     *
     * call this method in the [onStart()]
     *
     * @param updateUserUI is any generic lambda function that user update ui. Create a function that accept [FirebaseUser] and implement your UI logic.
     * @sample updateUI
     * */

    fun reloadUser(updateUserUI: (user: FirebaseUser?) -> Unit) {
        if (currentUser != null) {
            auth.currentUser!!.reload().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    updateUserUI(auth.currentUser)
                    Toast.makeText(context, "Reload successful!", Toast.LENGTH_SHORT).show()
                } else {
                    Log.e(TAG, "reload", task.exception)
                    Toast.makeText(context, "Failed to reload user.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /**
     * If a user has signed in successfully you can get their account data at any point with the getCurrentUser method.
     * To get the profile information retrieved from the sign-in providers linked to a user, use the [getProviderData] method.
     * @sample getUserPnPInfo
     * */

    fun accessUserProfileAndProvidedInfo(userInformation: (user: FirebaseUser?) -> Unit) = userInformation(currentUser)


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

}