package com.peacecodetech.medeli.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseUser
import com.peacecodetech.medeli.data.repository.FirebaseAuthRepository
import com.peacecodetech.medeli.model.User
import com.peacecodetech.medeli.util.Resource
import com.peacecodetech.medeli.util.isValidString
import com.peacecodetech.medeli.util.validatePassword
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val repository: FirebaseAuthRepository) : ViewModel() {

    /***
     * Observe user live data after login
     * */

    private val _userLiveDataObserver: MutableLiveData<Resource<User>?> = MutableLiveData<Resource<User>?>()
    val userLiveDataObserver: LiveData<Resource<User>?>
        get() = _userLiveDataObserver
    /**
     * Call this function to sign in user
     * @param email user email
     * @param password user password
     * **/


    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            if (validatePassword(password) and isValidString(email)) {
                repository.signUpWithEmail(email = email, password = password)
                _userLiveDataObserver.value = repository.getUserLiveData().value
            }else{
                _userLiveDataObserver.postValue(Resource.error(data = null,"Login error"))
            }

        }

    }
}
