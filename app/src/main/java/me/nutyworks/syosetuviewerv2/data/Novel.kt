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
    var readIndexes: String = "",
    var recentWatchedEpisode: Int = 0,
    var recentWatchedPercent: Float = 0f,
)

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE novels ADD COLUMN readIndexes TEXT NOT NULL DEFAULT ''")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE novels ADD COLUMN recentWatchedEpisode INT NOT NULL DEFAULT 0")
        database.execSQL("ALTER TABLE novels ADD COLUMN recentWatchedPercent FLOAT NOT NULL DEFAULT 0")
    }
}