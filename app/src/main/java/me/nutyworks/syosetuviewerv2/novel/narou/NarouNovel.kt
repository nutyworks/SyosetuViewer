package me.nutyworks.syosetuviewerv2.novel.narou

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelChapter
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelEntity
import me.nutyworks.syosetuviewerv2.novel.NovelProvider
import me.nutyworks.syosetuviewerv2.novel.NovelStatus
import me.nutyworks.syosetuviewerv2.novel.RootNovelContent
import org.jsoup.Jsoup

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
    override fun getContents(): List<NovelContent> =
        Jsoup.connect("https://ncode.syosetu.com/$identifier").cookie("over18", "yes").get().run {
            val novelBodyRegex =
                """(?:<div class="chapter_title">\s*(.+?)\s*</div>|<dl class="novel_sublist2">[\s\S]*?<dd class="subtitle">[\s\S]*?<a href="/.+?/(\d+)/">(.+?)</a>)""".toRegex()

            novelBodyRegex.findAll(select(".index_box").html()).map { result ->
                result.destructured.let { (chapterTitle, index, episodeTitle) ->
                    when {
                        chapterTitle.isNotEmpty() ->
                            NovelChapter(null, chapterTitle)
                        episodeTitle.isNotEmpty() ->
                            NarouNovelEpisode(null, title, this@NarouNovel, index.toInt())
                        else ->
                            throw IllegalStateException("Novel body is neither chapter nor episode.")
                    }
                }
            }
        }.toList().scan<NovelContent, NovelContent>(RootNovelContent) { acc, cur ->
            if (acc is RootNovelContent && cur is NovelChapter) {
                cur.copy(parent = acc)
            } else if (acc is NovelChapter && cur is NovelChapter) {
                cur.copy(parent = acc)
            } else if (acc is RootNovelContent && cur is NarouNovelEpisode) {
                cur.copy(parent = acc)
            } else if (acc is NovelChapter && cur is NarouNovelEpisode) {
                cur.copy(parent = acc)
            } else if (acc is NarouNovelEpisode && cur is NarouNovelEpisode) {
                cur.copy(parent = acc.parent)
            } else if (acc is NarouNovelEpisode && cur is NovelChapter) {
                cur.copy(parent = acc.parent?.parent ?: RootNovelContent)
            } else {
                throw IllegalStateException("Error occurred while arranging novel content.")
            }
        }.drop(1)

    override fun toNovelEntity(): NovelEntity =
        NovelEntity(identifier, NovelProvider.NAROU)
}
