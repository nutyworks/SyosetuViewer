package me.nutyworks.syosetuviewerv2.data

data class NovelBody(
    val body: String,
    val isChapter: Boolean,
    val index: Int,
    var translatedBody: String = ""
)