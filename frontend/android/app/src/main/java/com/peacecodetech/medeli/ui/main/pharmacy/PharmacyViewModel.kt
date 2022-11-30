package com.peacecodetech.medeli.ui.main.pharmacy

import androidx.lifecycle.*
import com.peacecodetech.medeli.data.repository.firebase.FirebaseRealtimeDBImpl
import com.peacecodetech.medeli.data.repository.room.PharmacyRepository
import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PharmacyViewModel @Inject constructor(
    private val repository: FirebaseRealtimeDBImpl,
    private val pharmacyRepository: PharmacyRepository
) : ViewModel() {

    /*private val _dataState = MutableLiveData<Resource<List<Pharmacy>>>(Resource.loading(null))
    val dataState: LiveData<Resource<List<Pharmacy>>> = _dataState*/

    private val _isDataAdded = MutableLiveData<Resource<Void?>>(Resource.success(null))
    val isDataAdded: LiveData<Resource<Void?>> = _isDataAdded
    var openDialogState = MutableLiveData(false)

    private val _isDataDeleted = MutableLiveData<Resource<Void?>>(Resource.success(null))
    val isDataDeleted: LiveData<Resource<Void?>> = _isDataDeleted

    private val _pharmacyData: MutableStateFlow<Resource<List<Pharmacy>>?> =
        MutableStateFlow(null)
    val pharmacyData = _pharmacyData.asStateFlow()

    private val _roomPharmacyData: MutableStateFlow<List<Pharmacy>?> =
        MutableStateFlow(null)
    val roomPharmacyDataData = _roomPharmacyData.asStateFlow()

    init {
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            repository.getPharmacyData()
                .collect { response ->
                    _pharmacyData.value = response
            }
        }
    }

    fun addData(pharmacy: Pharmacy) {
        viewModelScope.launch {
            repository.addPharmacyData(pharmacy)
                .collect { response ->
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

     val getRoomPharmacyData = pharmacyRepository.getAllPharmacy().asLiveData()


     fun insertIntoRoomPharmacyData(pharmacy: Pharmacy) =
        viewModelScope.launch {
            pharmacyRepository.insertToRoom(pharmacy)

        }


}