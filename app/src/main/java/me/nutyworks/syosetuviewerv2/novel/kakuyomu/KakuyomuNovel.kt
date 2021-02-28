package me.nutyworks.syosetuviewerv2.novel.kakuyomu

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelChapter
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelEntity
import me.nutyworks.syosetuviewerv2.novel.NovelProvider
import me.nutyworks.syosetuviewerv2.novel.NovelStatus
import me.nutyworks.syosetuviewerv2.novel.RootNovelContent
import org.jsoup.Jsoup

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
    override fun getContents(): List<NovelContent> =
        Jsoup.connect("https://kakuyomu.jp/works/$identifier").get().run {
            val novelBodyRegex =
                """(?:<li class="widget-toc-chapter.*?">[\n\r\s]*?<span>(.+?)</span>[\n\r\s]*?</li>|<li class="widget-toc-episode">[\n\r\s]*?<a href="/works/\d+/episodes/(\d+)"[\s\S]*?<span class=".*?">(.+?)</span>[\s\S]*?</li>)""".toRegex()

            novelBodyRegex.findAll(select(".widget-toc-items").html()).map { result ->
                result.destructured.let { (chapterTitle, index, episodeTitle) ->
                    when {
                        chapterTitle.isNotEmpty() ->
                            NovelChapter(null, chapterTitle)
                        episodeTitle.isNotEmpty() ->
                            KakuyomuNovelEpisode(null, episodeTitle, this@KakuyomuNovel, index)
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
            } else if (acc is RootNovelContent && cur is KakuyomuNovelEpisode) {
                cur.copy(parent = acc)
            } else if (acc is NovelChapter && cur is KakuyomuNovelEpisode) {
                cur.copy(parent = acc)
            } else if (acc is KakuyomuNovelEpisode && cur is KakuyomuNovelEpisode) {
                cur.copy(parent = acc.parent)
            } else if (acc is KakuyomuNovelEpisode && cur is NovelChapter) {
                cur.copy(parent = acc.parent?.parent ?: RootNovelContent)
            } else {
                println("$acc $cur")
                throw IllegalStateException("Error occurred while arranging novel content.")
            }
        }.drop(1)

    override fun toNovelEntity(): NovelEntity =
        NovelEntity(identifier, NovelProvider.KAKUYOMU)
}
