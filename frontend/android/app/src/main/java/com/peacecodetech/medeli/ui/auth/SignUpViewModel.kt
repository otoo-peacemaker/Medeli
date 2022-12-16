package com.peacecodetech.medeli.ui.auth

import android.content.ContentValues.TAG
import android.widget.Button
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.peacecodetech.medeli.data.repository.firebase.FirebaseAuthRepository
import com.peacecodetech.medeli.data.responses.User
import com.peacecodetech.medeli.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository,
    private var auth: FirebaseAuth
) : ViewModel() {
    private val userLiveData = MutableLiveData<Resource<User>>()

    fun registerUser(
        isEnable: Button? = null,
        email: String,
        password: String,
        phoneNumber:String,

    ): LiveData<Resource<User>> {

        try {
            if (email.isNotEmpty() and password.isNotEmpty() and phoneNumber.isNotEmpty()){
                Resource.loading(null)
                viewModelScope.launch {
                    repository.signUpWithEmail(email = email, password = password)
                        .addOnCompleteListener { task ->
                            userLiveData.postValue(Resource.loading(null))
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Timber.tag(TAG).d("createUserWithEmail:success")
                                sendEmailVerification()
                                val user = auth.currentUser
                                userLiveData.postValue(
                                    Resource.success(
                                        User(
                                            id = user!!.uid.toIntOrNull(),
                                            email = user.email,
                                            fullName = user.displayName,
                                            password = user.phoneNumber
                                        )
                                    )
                                )
                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.tag(TAG).w(task.exception, "createUserWithEmail:failure")
                                userLiveData.postValue(
                                    Resource.error(
                                        null,
                                        message = task.exception?.message.toString()
                                    )
                                )
                            }
                        }
                }
            }else {
                userLiveData.postValue(
                    Resource.error(
                        data = null,
                        "Please, fill all fields"
                    )
                )
            }

        }catch (e: RuntimeExecutionException) {
            userLiveData.postValue(Resource.error(null, e.message.toString()))
        }

        return userLiveData
    }


    private fun sendEmailVerification(isEnable: Button? = null) {
        // Disable button
        isEnable?.isEnabled = false
        // Send verification email
        repository.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                // Re-enable button
                isEnable?.isEnabled = true
                if (task.isSuccessful) {
                    userLiveData.postValue(Resource.success(User()))
                } else {
                    Timber.tag(TAG).e(task.exception, "sendEmailVerification")
                    userLiveData.postValue(Resource.error(null, task.exception?.message.toString()))
                }
            }
    }

    fun saveUser(fullName: String, email: String, password: String) {
        repository.saveUser(fullName, email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                userLiveData.postValue(Resource.success(User()))
            } else {
                userLiveData.postValue(Resource.error(null, task.exception?.message.toString()))
            }
        }
    }
/*
    fun signUpWithGoogle(string: String): LiveData<Resource<User>> {
        repository.signInWithGoogle(string)
        return userLiveData
    }*/
}