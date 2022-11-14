package com.peacecodetech.medeli.data.repository

import android.widget.Button
import com.google.firebase.auth.FirebaseUser
import com.peacecodetech.medeli.data.source.FirebaseDatastore

class UserAuthenticationRepository(
    private val firebaseDatastore: FirebaseDatastore

) {

    fun loginUser(email: String, password: String, updateUserUI: (user: FirebaseUser?) -> Unit) {
        return firebaseDatastore.signInExistingUserWithEmail(
            email = email,
            password = password,
            updateUserUI = updateUserUI
        )
    }

    fun signUpUser(email: String, password: String, updateUserUI: (user: FirebaseUser?) -> Unit) {
        return firebaseDatastore.signUpWithEmail(
            email = email,
            password = password,
            updateUserUI = updateUserUI
        )
    }

    fun sendEmailVerification(button: Button){
        return firebaseDatastore.sendEmailVerification(button)
    }

    fun signInWithGoogle(clientId:String){
        return firebaseDatastore.signInWithGoogle(clientId)
    }

    fun reloadUser(updateUserUI: (user: FirebaseUser?) -> Unit){
        return firebaseDatastore.reloadUser { user -> updateUserUI(user) }
    }

    fun oneTapSignInWithGoogle(clientId:String){
        return firebaseDatastore.oneTapSignInWithGoogle(clientId)
    }

    fun signOut(updateUserUI: (user: FirebaseUser?) -> Unit){
        return firebaseDatastore.signOut(updateUserUI)
    }
}