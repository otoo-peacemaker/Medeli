package com.peacecodetech.medeli.data.source.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

//Adding or removing from column
val ALTER_TABLE_MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE cart ADD COLUMN quantity INTEGER DEFAULT 1 not null ")
    }

}


/**Changing the column name either id or username or deleting column takes the following steps
 * create a new temporary table with the new schema,
 * copy the data from the users table to the temporary table,
 * drop the users table
 * rename the temporary table to users
 * */
val ALTER_COLUMN_MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        //We're trying to change the user id into something different a name
        //Create a new temp table
        database.execSQL("CREATE TABLE newTable (id TEXT, username TEXT, age INTEGER, PRIMARY KEY(id))");

        //copy the data
        database.execSQL("INSERT INTO newTable(id, username, age) SELECT old_id, username, age FROM oldTable")

        //Remove the old table
        database.execSQL("DROP TABLE oldTable")

        //Change the table to teh correct one
        database.execSQL("ALTER TABLE newTable RENAME TO oldTable")
    }

}