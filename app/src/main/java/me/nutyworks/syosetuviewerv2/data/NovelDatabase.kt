package me.nutyworks.syosetuviewerv2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Novel::class], version = 2, exportSchema = false)
abstract class NovelDatabase : RoomDatabase() {

    abstract fun novelDao(): NovelDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: NovelDatabase? = null

        fun getInstance(context: Context): NovelDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        // Create and pre-populate the database. See this article for more details:
        // https://medium.com/google-developers/7-pro-tips-for-room-fbadea4bfbd1#4785
        private fun buildDatabase(context: Context): NovelDatabase {
            return Room.databaseBuilder(context, NovelDatabase::class.java, "novels")
                .addMigrations(MIGRATION_1_2)
                .build()
        }
    }
}