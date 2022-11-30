package com.peacecodetech.medeli.data.repository

import androidx.lifecycle.asLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.Query
import com.peacecodetech.medeli.data.repository.firebase.FirebaseRealtimeDB
import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.util.Resource
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseRealtimeDBImpl @Inject constructor(
    private val ref: CollectionReference,
    private val query: Query
): FirebaseRealtimeDB {
    override fun getPharmacyData()= callbackFlow {
        val snapshotListener = query.addSnapshotListener { snapshot, e ->
            val response = if (snapshot != null) {
                val pharmacies = snapshot.toObjects(Pharmacy::class.java)
                Resource.success(pharmacies)
            } else {
                Resource.error(null,e?.message ?: e.toString())
            }
            trySend(response).isSuccess
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addPharmacyData(pharmacy: Pharmacy)= flow {
        try {
            emit(Resource.loading(null))
            val pharmacyId = ref.document().id
            val metaData = Pharmacy(pharmacy.logo,pharmacy.name, pharmacy.distance, pharmacy.description,pharmacy.rating)
            val addition = ref.document(pharmacyId).set(metaData).await()
            emit(Resource.success(addition))
        } catch (e: Exception) {
            Resource.error(null, e.message ?: e.toString())
        }
    }

    override suspend fun deletePharmacyData(pharmacyId: String) = flow {
        try {
            emit(Resource.loading(null))
            val deletion = ref.document(pharmacyId).delete().await()
            emit(Resource.success(deletion))
        } catch (e: Exception) {
            Resource.error(null, e.message ?: e.toString())
        }
    }
}