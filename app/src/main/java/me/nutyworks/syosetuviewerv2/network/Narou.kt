package me.nutyworks.syosetuviewerv2.network

import me.nutyworks.syosetuviewerv2.data.ImageWrapper
import me.nutyworks.syosetuviewerv2.data.Novel
import me.nutyworks.syosetuviewerv2.data.NovelBody
import me.nutyworks.syosetuviewerv2.data.wrap
import org.jsoup.Jsoup

object Narou {

    private const val TAG = "Narou"

    fun getNovel(ncode: String, isOver18: Boolean): Novel =
        Jsoup.connect("https://ncode.syosetu.com/$ncode").cookie("over18", "yes").get().run {
            val title = select(".novel_title").html()
            val writer = select(".novel_writername > a").html().let {
                if (it.isNullOrEmpty()) select(".novel_writername").html()
                    .replace("作者：", "") else it
            }
            val isR18 = select(".contents1").html().contains("＜R18＞")

            if (isR18 && !isOver18) throw IllegalAccessException("You must be 18 or higher.")

            Novel(
                ncode,
                title,
                PapagoRequester.request("ja-ko", title),
                writer,
                isR18,
            )
        }

    fun getNovelBodies(ncode: String): List<NovelBody> =
        Jsoup.connect("https://ncode.syosetu.com/$ncode").cookie("over18", "yes").get().run {
            val novelBodyRegex =
                """(?:<div class="chapter_title">\s*(.+?)\s*</div>|<dl class="novel_sublist2">[\s\S]*?<dd class="subtitle">[\s\S]*?<a href="/.+?/(\d+)/">(.+?)</a>)""".toRegex()

            novelBodyRegex.findAll(select(".index_box").html()).map { result ->
                result.destructured.let { (chapterTitle, index, episodeTitle) ->
                    when {
                        chapterTitle.isNotEmpty() -> NovelBody(chapterTitle.wrap(), true, 0)
                        episodeTitle.isNotEmpty() ->
                            NovelBody(episodeTitle.wrap(), false, index.toInt())
                        else -> throw IllegalStateException("Novel body is neither chapter nor episode.")
                    }
                }
            }
        }.toList()

    fun getNovelBody(ncode: String, index: Int): NovelBody =
        Jsoup.connect("https://ncode.syosetu.com/$ncode/$index").cookie("over18", "yes").get().run {
            val body = select(".novel_subtitle").eachText().first()
            val imgRegex =
                """<img src="(.+?)" alt="(.+?)"[\s\S]*>""".toRegex()
            val mainTextWrappers =
                select("#novel_honbun > p").filter {
                    it.text().isNotBlank() || it.html().contains(imgRegex)
                }.map {
                    imgRegex.find(it.html())?.let { match ->
                        ImageWrapper(match.groupValues[1], match.groupValues[2])
                    } ?: it.text().wrap()
                }

            NovelBody(
                body.wrap(),
                false,
                index,
                mainTextWrappers
            )
        }
}
