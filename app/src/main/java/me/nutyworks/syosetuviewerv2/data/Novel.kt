package me.nutyworks.syosetuviewerv2.data

import kotlinx.coroutines.delay

interface INovel {
    val title: String
    val writer: String
    val identifier: String

    override fun equals(other: Any?): Boolean
}

data class ChapteredNovel(
    override val title: String,
    override val writer: String,
    override val identifier: String,
    val chapters: List<Chapter>,
) : INovel {

}

data class NonChapterNovel(
    override val title: String,
    override val writer: String,
    override val identifier: String,
    val episodes: List<Episode>
) : INovel

data class Chapter(
    val title: String,
    val episodes: List<Episode>,
)

data class Episode(
    val title: String,
    val index: Int,
) {
    lateinit var mainText: List<String>
}