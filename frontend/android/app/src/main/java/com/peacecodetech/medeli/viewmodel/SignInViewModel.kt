package com.peacecodetech.medeli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import com.peacecodetech.medeli.data.repository.FirebaseAuthRepository
import com.peacecodetech.medeli.util.isValidString
import com.peacecodetech.medeli.util.validatePassword
import kotlinx.coroutines.launch

class SignInViewModel(
    private val repository: FirebaseAuthRepository) : ViewModel() {

    /***
     * Observe user live data after login
     * */
    private val _userLiveDataUser = MutableLiveData<FirebaseUser>()
    val firebaseUserLiveDataObserver: LiveData<FirebaseUser>
        get() = _userLiveDataUser
    /**
     * Call this function to sign in user
     * @param email user email
     * @param password user password
     * **/


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            if (validatePassword(password) and isValidString(email)){
                _userLiveDataUser.value = repository.getUserLiveData()
            }
        }
        return repository.signUpWithEmail(email = email, password = password)
    }
}