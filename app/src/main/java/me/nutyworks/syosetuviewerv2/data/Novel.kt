package me.nutyworks.syosetuviewerv2.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Entity(tableName = "novels")
data class Novel(
    @PrimaryKey @NonNull val ncode: String,
    val title: String,
    val translatedTitle: String,
    val writer: String,
    var readIndexes: String = ""
)

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE novels ADD COLUMN readIndexes TEXT NOT NULL DEFAULT ''"
        )
    }
}