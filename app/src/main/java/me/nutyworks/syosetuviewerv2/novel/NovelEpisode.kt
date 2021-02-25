package me.nutyworks.syosetuviewerv2.novel

interface NovelEpisode : NovelContent {
    val novel: Novel
    val index: Any

    fun getPreText(): List<String>
    fun getMainText(): List<String>
    fun getPostText(): List<String>
}
