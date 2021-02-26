package me.nutyworks.syosetuviewerv2.novel.narou

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelStatus

data class NarouNovel(
    override val identifier: String,
    override val title: String,
    override val author: String,
    override val status: NovelStatus,
    override val description: String,
    override val keywords: List<String>,
    override val genre: String,
    override val nCharacters: Int,
    val isR18: Boolean,
) : Novel {
    override fun getContents(): List<NovelContent> {
        TODO("move Narou to here")
    }
}
