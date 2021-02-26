package me.nutyworks.syosetuviewerv2.novel

data class NovelChapter(
    override val parent: NovelContent?,
    override val name: String,
) : NovelContent(parent, name)
