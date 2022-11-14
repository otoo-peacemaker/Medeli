package com.peacecodetech.medeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.peacecodetech.medeli.data.source.FirebaseDatastore

class MainActivity : AppCompatActivity() {
    private val firebaseAuth: FirebaseAuth? = null
    private var signInClient: SignInClient? = null
    private val firebaseDatastore = firebaseAuth?.let { FirebaseDatastore(it, signInClient!!) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val email = "demagogue"
        val password = "passenger"
        firebaseDatastore?.signUpWithEmail(email = email, password = password) {
            //update the ui
        }

        firebaseDatastore?.signOut {
            //update the ui
        }


    }
}