package me.nutyworks.syosetuviewerv2.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NovelDao {

    @Query("SELECT * FROM novels ORDER BY ncode ASC")
    fun getAll(): LiveData<List<Novel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(novel: Novel)

    @Delete
    suspend fun delete(novel: Novel)

    @Query("DELETE FROM novels")
    suspend fun deleteAll()
}