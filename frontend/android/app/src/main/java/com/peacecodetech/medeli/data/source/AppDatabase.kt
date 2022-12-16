package com.peacecodetech.medeli.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import com.peacecodetech.medeli.data.dao.PharmacyDao
import com.peacecodetech.medeli.data.model.Pharmacy

@Database(
    entities = [
        Pharmacy::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pharmacyDao(): PharmacyDao
}