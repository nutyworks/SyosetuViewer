package me.nutyworks.syosetuviewerv2.novel

interface NovelParser {
    fun getNovel(identifier: String): Novel
    fun getTableOfContents(identifier: String): NovelContent
}
