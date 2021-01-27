package me.nutyworks.syosetuviewerv2.data

import java.io.Serializable

data class SearchRequirements(
    val includeWords: String,
    val excludeWords: String,
    val genres: List<Int>,
    val requireType: String,
    val minTime: Int?,
    val maxTime: Int?,
    val minLen: Int?,
    val maxLen: Int?,
    val minGlobalPoint: Int?,
    val maxGlobalPoint: Int?,
    val minLastUp: String,
    val maxLastUp: String,
    val minFirstUp: String,
    val maxFirstUp: String,
    val orderBy: Int,
) : Serializable
