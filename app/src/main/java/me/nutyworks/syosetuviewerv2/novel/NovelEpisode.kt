package me.nutyworks.syosetuviewerv2.novel

abstract class NovelEpisode(
    parent: NovelContent?,
    name: String,
    open val novel: Novel,
    open val index: Any,
) : NovelContent(parent, name) {
    abstract fun getPreText(): List<String>
    abstract fun getMainText(): List<String>
    abstract fun getPostText(): List<String>
}
