package com.peacecodetech.medeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.peacecodetech.medeli.data.repository.UserAuthenticationRepository
import com.peacecodetech.medeli.data.source.FirebaseDatastore

class MainActivity : AppCompatActivity() {
    private val firebaseAuth: FirebaseAuth? = null
    private var signInClient: SignInClient? = null

    private val repository: UserAuthenticationRepository ?=null

    private val firebaseDatastore = firebaseAuth?.let { FirebaseDatastore(it, signInClient!!) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val email = "demagogue"
        val password = "passenger"
        repository?.getUserProfile {
            it?.let {
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
            firebaseDatastore?.signUpWithEmail(email = email, password = password) {
                //update the ui
            }

            firebaseDatastore?.signOut {
                //update the ui
            }


        }
    }
}