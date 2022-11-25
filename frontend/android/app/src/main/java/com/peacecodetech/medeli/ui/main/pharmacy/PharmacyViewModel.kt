package com.peacecodetech.medeli.ui.main.pharmacy

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peacecodetech.medeli.data.repository.FirebaseRealtimeDBImpl
import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PharmacyViewModel @Inject constructor(
    private val repository: FirebaseRealtimeDBImpl
) : ViewModel() {

    private val _dataState = MutableLiveData<Resource<List<Pharmacy>>>(Resource.loading(null))
    val dataState: LiveData<Resource<List<Pharmacy>>> = _dataState

    private val _isDataAdded = MutableLiveData<Resource<Void?>>(Resource.success(null))
    val isDataAdded: LiveData<Resource<Void?>> = _isDataAdded
    var openDialogState = MutableLiveData(false)

    private val _isDataDeleted = MutableLiveData<Resource<Void?>>(Resource.success(null))
    val isDataDeleted: LiveData<Resource<Void?>> = _isDataDeleted

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            repository.getPharmacyData().collect { response ->
                _dataState.value = response
            }
        }
    }

    fun addData(pharmacy: Pharmacy) {
        viewModelScope.launch {
            repository.addPharmacyData(pharmacy).collect { response ->
                _isDataAdded.value = response
            }
        }
    }

    fun deleteData(bookId: String) {
        viewModelScope.launch {
            repository.deletePharmacyData(bookId).collect { response ->
                _isDataDeleted.value = response
            }
        }
    }
}