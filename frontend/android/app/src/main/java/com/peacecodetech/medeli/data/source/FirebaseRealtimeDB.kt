package com.peacecodetech.medeli.data.repository.firebase

import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.util.Resource
import kotlinx.coroutines.flow.Flow

interface FirebaseRealtimeDB {

    fun getPharmacyData(): Flow<Resource<List<Pharmacy>>>

    suspend fun addPharmacyData(pharmacy: Pharmacy): Flow<Resource<Void?>>

    suspend fun deletePharmacyData(pharmacyId: String): Flow<Resource<Void?>>
}