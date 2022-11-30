package com.peacecodetech.medeli.data.repository.room

import com.peacecodetech.medeli.data.dao.PharmacyDao
import com.peacecodetech.medeli.model.Pharmacy
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PharmacyRepository @Inject constructor(
    private val pharmacyDao: PharmacyDao
):PharmacyDao {
    override suspend fun insertToRoom(pharmacy: Pharmacy) {
       return pharmacyDao.insertToRoom(pharmacy)
    }

    override suspend fun updateList(pharmacy: Pharmacy) {
        return pharmacyDao.updateList(pharmacy)
    }

    override fun getAllPharmacy(): Flow<List<Pharmacy>> {
     return pharmacyDao.getAllPharmacy()
    }

    override suspend fun deleteAllFromPharmacy() {
       return pharmacyDao.deleteAllFromPharmacy()
    }
}