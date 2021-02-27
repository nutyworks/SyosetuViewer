package me.nutyworks.syosetuviewerv2.novel.kakuyomu

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelEpisode
import me.nutyworks.syosetuviewerv2.novel.NovelEpisodeComponent
import org.jsoup.Jsoup

data class KakuyomuNovelEpisode(
    override val parent: NovelContent?,
    override val name: String,
    override val novel: Novel,
    override val index: String,
) : NovelEpisode(parent, name, novel, index) {
    override fun getPreText(): List<NovelEpisodeComponent> = listOf()

    override fun getMainText(): List<NovelEpisodeComponent> =
        Jsoup.connect("https://kakuyomu.jp/works/${novel.identifier}/episodes/$index")
            .get().run {
                val imgRegex =
                    """^[\s\u3000]*(https?://.+)[\s\u3000]*$""".toRegex()

                select(".widget-episodeBody > p").map { element ->
                    imgRegex.matchEntire(element.html())?.let { match ->
                        return@map NovelEpisodeComponent.Image(match.groupValues[1], "")
                    }

                    return@map element.text().let {
                        if (it.isBlank()) NovelEpisodeComponent.Blank
                        else NovelEpisodeComponent.Text(it)
                    }
                }
            }

    override fun getPostText(): List<NovelEpisodeComponent> = listOf()
}
