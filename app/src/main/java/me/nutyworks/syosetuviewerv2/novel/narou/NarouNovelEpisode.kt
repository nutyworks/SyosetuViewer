package me.nutyworks.syosetuviewerv2.novel.narou

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelContent
import me.nutyworks.syosetuviewerv2.novel.NovelEpisode
import me.nutyworks.syosetuviewerv2.novel.NovelEpisodeComponent
import org.jsoup.Jsoup

data class NarouNovelEpisode(
    override val parent: NovelContent?,
    override val name: String,
    override val novel: Novel,
    override val index: Int,
) : NovelEpisode(parent, name, novel, index) {
    override fun getPreText(): List<NovelEpisodeComponent> =
        getComponentWithSelector("#novel_p > p")

    override fun getMainText(): List<NovelEpisodeComponent> =
        getComponentWithSelector("#novel_honbun > p")

    override fun getPostText(): List<NovelEpisodeComponent> =
        getComponentWithSelector("#novel_a > p")

    private fun getComponentWithSelector(selector: String) =
        Jsoup.connect("https://ncode.syosetu.com/${novel.identifier}/$index")
            .cookie("over18", "yes").get().run {
                val imgRegex =
                    """<img src="(.+?)" alt="(.+?)"[\s\S]*>""".toRegex()

                select(selector).map { element ->
                    imgRegex.find(element.html())?.let { match ->
                        return@map NovelEpisodeComponent.Image(
                            match.groupValues[1],
                            match.groupValues[2]
                        )
                    }

                    return@map element.text().let {
                        if (it.isEmpty()) NovelEpisodeComponent.Blank
                        else NovelEpisodeComponent.Text(it)
                    }
                }
            }
}
