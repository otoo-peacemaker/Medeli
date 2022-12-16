package com.peacecodetech.medeli.ui.auth

import android.content.ContentValues
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.firebase.auth.FirebaseAuth
import com.peacecodetech.medeli.data.repository.firebase.FirebaseAuthRepository
import com.peacecodetech.medeli.data.responses.User
import com.peacecodetech.medeli.util.NetworkManager
import com.peacecodetech.medeli.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel
@Inject constructor(
    private val networkControl: NetworkManager,
    private val repository: FirebaseAuthRepository,
    auth: FirebaseAuth
) : ViewModel() {

    /***
     * Observe user live data after login
     * */

    private val _userLiveDataObserver: MutableLiveData<Resource<User>?> = MutableLiveData<Resource<User>?>()
    private val user = auth.currentUser
    fun loginUser(email: String, password: String): LiveData<Resource<User>?> {
        viewModelScope.launch {
            try {
                if (email.isNotEmpty() and password.isNotEmpty()) {
                    Resource.loading(null)
                    repository.signInExistingUserWithEmail(email = email, password = password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                //TODO:  CHECK IF EMAIL IS VERIFIED
                                Timber.tag(ContentValues.TAG).d("signInWithEmail:success ${task.result}")
                                //   _updateUserUI.postValue(currentUser)
                                _userLiveDataObserver.postValue(
                                    Resource.success(
                                        User(
                                            uid = user?.uid,
                                            email = user?.email,
                                        )
                                    )
                                )
                            } else {
                                // If sign in fails, display a message to the user.
                                Timber.tag(ContentValues.TAG)
                                    .w(task.exception, "signInWithEmail:failure")
                                _userLiveDataObserver.postValue(
                                    Resource.error(
                                        null,
                                        message = task.exception?.message.toString()
                                    )
                                )
                            }
                        }
                    _userLiveDataObserver.value = repository.getUserLiveData().value
                } else {
                    _userLiveDataObserver.postValue(
                        Resource.error(
                            data = null,
                            "Please, fill all fields"
                        )
                    )
                }
            } catch (e: RuntimeExecutionException) {
                _userLiveDataObserver.postValue(Resource.error(null, e.message.toString()))
            }
        }
        return _userLiveDataObserver
    }

    fun signInWithGoogle(acct: GoogleSignInAccount): LiveData<Resource<User>?> {
        Resource.loading(null)
        repository.signInWithGoogle(acct).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _userLiveDataObserver.postValue(
                    Resource.success(
                        User(
                            email = user?.email,
                            fullName = user?.displayName
                        )
                    )
                )
            } else {
                _userLiveDataObserver.postValue(Resource.error(null, "couldn't sign in user"))
            }
        }
        return _userLiveDataObserver
    }

    fun sendPasswordResetEmail(email: String): LiveData<Resource<User>?> {
        Resource.loading(null)
        if (TextUtils.isEmpty(email)) {
            _userLiveDataObserver.postValue(Resource.error(null, "Enter registered email"))
        } else if (networkControl.isConnected()) {
            repository.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                _userLiveDataObserver.postValue(Resource.loading(null))
                if (task.isSuccessful) {
                    _userLiveDataObserver.postValue(Resource.success(User()))
                } else {
                    _userLiveDataObserver.postValue(
                        Resource.error(
                            null,
                            task.exception?.message.toString()
                        )
                    )
                }
            }
        } else {
            _userLiveDataObserver.postValue(Resource.error(null, "No internet connection"))
        }
        return _userLiveDataObserver
    }


}
