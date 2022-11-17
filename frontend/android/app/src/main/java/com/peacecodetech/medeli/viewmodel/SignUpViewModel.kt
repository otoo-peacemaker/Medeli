package com.peacecodetech.medeli.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.peacecodetech.medeli.data.repository.FirebaseAuthRepository
import com.peacecodetech.medeli.model.User
import com.peacecodetech.medeli.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository
) : ViewModel() {
    private val userLiveData = MutableLiveData<Resource<User>>()

    fun registerUser(
        isEnable: Button? = null,
        email: String,
        password: String
    ): LiveData<Resource<User>> {
        repository.signUpWithEmail(email = email, password = password)
            .addOnCompleteListener { task ->
                userLiveData.postValue(Resource.loading(null))
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Timber.tag(TAG).d("createUserWithEmail:success")
                    repository.sendEmailVerification()
                    userLiveData.postValue(
                        Resource.success(
                            User(
                                id = task.result.user!!.uid.toInt(),
                                email = task.result.user?.email,
                                fullName = task.result.user?.displayName,
                                password = task.result.user?.phoneNumber
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
        return userLiveData
    }


    fun sendEmailVerification(isEnable: Button? = null){
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
        repository.saveUser(fullName,email, password).addOnCompleteListener {task->
            if (task.isSuccessful) {
                userLiveData.postValue(Resource.success(User()))
            } else {
                userLiveData.postValue(Resource.error(null, task.exception?.message.toString()))
            }
        }
    }

    fun signUpWithGoogle(string: String):LiveData<Resource<User>>{
        repository.signInWithGoogle(string)
        return userLiveData
    }



}