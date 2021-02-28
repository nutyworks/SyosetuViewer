package me.nutyworks.syosetuviewerv2.novel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "novel_user_data")
data class NovelUserData(
    @PrimaryKey val identifier: String,
    val readEpisodes: List<Any>,
    val lastReadEpisode: Any,
    val lastReadPercentage: Float,
)
