package me.nutyworks.syosetuviewerv2.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NovelEntityDao {

    @Query("SELECT * FROM novels ORDER BY ncode ASC")
    fun getAll(): LiveData<List<NovelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(novel: NovelEntity)

    @Delete
    suspend fun delete(novel: NovelEntity)

    @Query("DELETE FROM novels")
    suspend fun deleteAll()
}