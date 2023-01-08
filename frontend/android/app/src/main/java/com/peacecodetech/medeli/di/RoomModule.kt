package com.peacecodetech.medeli.di

import android.content.Context
import androidx.room.Room
import com.peacecodetech.medeli.data.dao.PharmacyDao
import com.peacecodetech.medeli.data.repository.room.PharmacyRepository
import com.peacecodetech.medeli.data.source.local.ALTER_TABLE_MIGRATION_1_2
import com.peacecodetech.medeli.data.source.local.AppDatabase
import com.peacecodetech.medeli.data.source.local.DatabaseCallback
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton


@Module//binds this to hilt
@InstallIn(SingletonComponent::class)//Install it in app container

object RoomModule {
    @Volatile
    var databaseInstance: AppDatabase? = null

    //providing database daos instances to hilt
    @Provides
    @Singleton
    fun providesPharmacyDao(productDatabase: AppDatabase): PharmacyDao =
        productDatabase.pharmacyDao()


    //providing pharmacy repository
    @Provides
    fun providesProductRepository(
        pharmacyDao: PharmacyDao
    ): PharmacyRepository = PharmacyRepository(pharmacyDao)


    @Singleton
    @Provides
    fun providesCoroutineScope(): CoroutineScope {
        // Run this code when providing an instance of CoroutineScope
        return CoroutineScope(SupervisorJob() + Dispatchers.Default)
    }

    @Provides
    @Singleton
    fun providesDatabase(@ApplicationContext context: Context, scope: CoroutineScope): AppDatabase {
        return databaseInstance ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context, AppDatabase::class.java, "medeli"
            ).allowMainThreadQueries()
                .addMigrations(ALTER_TABLE_MIGRATION_1_2)
                .addCallback(DatabaseCallback(context, scope))
                .build()
            databaseInstance = instance
            // return instance
            instance
        }
    }


}