package com.peacecodetech.medeli.data.repository


import android.content.ContentValues.TAG
import android.net.Uri
import android.telecom.Call.Details
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.peacecodetech.medeli.model.User
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.Resource
import timber.log.Timber
import javax.inject.Inject

class FirebaseAuthRepository @Inject constructor(
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
     * A function to sign up user in our database
     * @param isEnable enable or disable your UI buttons
     * @param email user email
     * @param password user password
     * @sample updateUI
     * */
    fun signUpWithEmail(isEnable: Button? = null, email: String, password: String) =
        auth.createUserWithEmailAndPassword(email, password)


    /**
     * @param isEnable enable or disable your UI buttons
     * */
    fun sendEmailVerification(isEnable: Button? = null) = currentUser?.sendEmailVerification()

    /**
     * @param email enter email to send a password reset
     * */
    fun sendPasswordResetEmail(email: String) = auth.sendPasswordResetEmail(email)


    /**
     * A function to sign user in our database
     * @param email user email
     * @param password user password
     * @param isEnable enable or disable your UI buttons
     * @sample updateUI
     * */

    fun signInExistingUserWithEmail(isEnable: Boolean? = null, email: String, password: String) =
        auth.signInWithEmailAndPassword(email, password)


    fun signInWithGoogle(acct: GoogleSignInAccount) = auth.signInWithCredential(
        GoogleAuthProvider.getCredential(acct.idToken, null)
    )


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

    fun saveUser(fullName: String, email: String, password: String) =
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
     * To get the profile information retrieved from the sign-in providers linked to a user, use the [getUserPnPInfo] method.
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

    /**
     *  Id of the provider (ex: google.com)
     *  UID specific to the provider
     *  Name, email address, and profile photo Url
     *
     * */

     fun getUserPnPInfo(details:(providerId:String, displayName:String?, email:String?,photoUrl:Uri?)->Unit) {
        currentUser?.let {
            for (profile in it.providerData) {
                details(profile.providerId,profile.displayName,profile.email,profile.photoUrl)
            }
        }
    }


    // private val updateUserUI: (user: MutableLiveData<FirebaseUser?>) -> Unit
}