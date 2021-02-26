package me.nutyworks.syosetuviewerv2.novel.kakuyomu

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelStatus

data class KakuyomuNovel(
    override val identifier: String,
    override val title: String,
    override val author: String,
    override val status: NovelStatus,
    override val description: String,
    override val keywords: List<String>,
    override val genre: String,
    override val nCharacters: Int
) : Novel {
    override fun getContents(): List<NovelContent> {
        TODO("not implemented")
    }
}
