package com.peacecodetech.medeli.data.dao

import androidx.room.*
import com.peacecodetech.medeli.model.Pharmacy
import kotlinx.coroutines.flow.Flow


@Dao
interface PharmacyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertToRoom(pharmacy: MutableList<Pharmacy>)

    @Update
    suspend fun updateList(pharmacy: Pharmacy)

    @Query("SELECT * FROM pharmacy ORDER BY id ASC")
    fun getAllPharmacy(): Flow<List<Pharmacy>>

    @Query("DELETE FROM pharmacy")
    suspend fun deleteAllFromPharmacy()
}