package com.peacecodetech.medeli.data.responses

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "pharmacy")
class Pharmacy(
    @PrimaryKey(autoGenerate = true) var id: Int? = 0,
    @ColumnInfo val logo: String? = null,
    @ColumnInfo val name: String? = null,
    @ColumnInfo val distance: String? = null,
    @ColumnInfo val description: String? = null,
    @ColumnInfo val rating: Int? = null,
)