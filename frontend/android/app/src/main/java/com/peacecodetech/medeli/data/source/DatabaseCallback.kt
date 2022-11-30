package com.peacecodetech.medeli.data.source

import android.content.Context
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.peacecodetech.medeli.data.dao.PharmacyDao
import com.peacecodetech.medeli.di.RoomModule
import com.peacecodetech.medeli.model.Pharmacy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import timber.log.Timber

@Suppress("BlockingMethodInNonBlockingContext")
class DatabaseCallback(
    private val application: Context,
    private val scope: CoroutineScope
) : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)
        RoomModule.databaseInstance.let{ database ->
            scope.launch {
                populatePharmacy(database!!.pharmacyDao())
            }
        }
    }//

    private suspend fun populatePharmacy(pharmacyDao: PharmacyDao) {
        val bufferReader = application.assets.open("pharmacy_metadata.json").bufferedReader()
        val jsonString = bufferReader.use {
            it.readText()
        }
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
            val name = jsonObject.getString("name")
            val description = jsonObject.getString("description")
            val imgUrl = jsonObject.getString("logo")
            val rating = jsonObject.getString("rating")
            val pharmacy = Pharmacy(
                logo = imgUrl,
                name = name,
                description = description,
                rating = rating.toIntOrNull(),
            )
            Timber.d("image: $imgUrl  name: $description || version : $name  \n")
            pharmacyDao.insertToRoom(pharmacy)
            Timber.d("image: $imgUrl  name: $description || version : $name  \n")
        }
    }

}