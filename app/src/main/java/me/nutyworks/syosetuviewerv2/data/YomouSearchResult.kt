package me.nutyworks.syosetuviewerv2.data

data class YomouSearchResult(
    val title: String,
    val writer: String,
    val ncode: String,
    val status: String,
    val episodes: Int,
    val description: String,
    val genre: String,
    val keywords: List<String>
)
