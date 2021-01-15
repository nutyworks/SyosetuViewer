package me.nutyworks.syosetuviewerv2.data

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "novels")
data class NovelEntity(
    @PrimaryKey @NonNull val ncode: String,
    val title: String,
    val translatedTitle: String,
    val writer: String,
)
