package me.nutyworks.syosetuviewerv2.novel.narou

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelEpisode

data class NarouNovelEpisode(
    override val parent: NovelContent?,
    override val name: String,
    override val novel: Novel,
    override val index: Int,
) : NovelEpisode(parent, name, novel, index) {
    override fun getPreText(): List<String> {
        TODO("not implemented")
    }

    override fun getMainText(): List<String> {
        TODO("not implemented")
    }

    override fun getPostText(): List<String> {
        TODO("not implemented")
    }
}
