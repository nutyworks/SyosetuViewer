package me.nutyworks.syosetuviewerv2.network

import com.nutyworks.syosetuviewer.translator.PapagoRequester
import me.nutyworks.syosetuviewerv2.data.Novel
import me.nutyworks.syosetuviewerv2.data.NovelBody
import org.jsoup.Jsoup

object Narou {

    private const val TAG = "Narou"

    fun getNovel(ncode: String): Novel =
        Jsoup.connect("https://ncode.syosetu.com/$ncode").get().run {
            val title = select(".novel_title").html()
            Novel(
                ncode,
                title,
                PapagoRequester.request(title),
                select(".novel_writername > a").html()
            )
        }

    fun getNovelBodies(ncode: String): List<NovelBody> =
        Jsoup.connect("https://ncode.syosetu.com/$ncode").get().run {
            val novelBodyRegex = """(?:<div class="chapter_title">\s*(.+?)\s*</div>|<dl class="novel_sublist2">[\s\S]*?<dd class="subtitle">[\s\S]*?<a href="/.+?/(\d+)/">(.+?)</a>)""".toRegex()

            novelBodyRegex.findAll(select(".index_box").html()).map { result ->
                result.groups[1]?.let {
                    return@map NovelBody(it.value, true, 0)
                }
                result.groups[3]?.let { title -> result.groups[2]?.let { index ->
                    return@map NovelBody(title.value, false, index.value.toInt())
                }}

                throw IllegalStateException("Novel body is neither chapter nor episode.")
            }.toList()
        }

    fun getNovelBody(ncode: String, index: Int): List<String> {
        return Jsoup.connect("https://ncode.syosetu.com/$ncode/$index").get().runCatching {
            this.select("#novel_honbun > p").eachText().map {
                it.replace("&lt;", "<")
                    .replace("&gt;", ">")
                    .replace("&amp;", "&")
            }
        }.getOrThrow()
    }
}