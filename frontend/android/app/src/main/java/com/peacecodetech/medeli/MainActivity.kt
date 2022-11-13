package com.peacecodetech.medeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.peacecodetech.medeli.data.FirebaseDatastore

class MainActivity : AppCompatActivity() {
    private val firebaseAuth: FirebaseAuth? = null
    private val firebaseDatastore = firebaseAuth?.let { FirebaseDatastore(it) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val email = "emaoofog"
        val password = "passwerpr"
        firebaseDatastore?.signUpNewUsers(email = email, password = password) {

        }

        firebaseDatastore?.signOut {

        }


    }
}