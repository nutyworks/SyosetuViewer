package me.nutyworks.syosetuviewerv2.network

import me.nutyworks.syosetuviewerv2.data.Novel
import me.nutyworks.syosetuviewerv2.data.NovelBody
import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import org.jsoup.Jsoup

object Narou {

    private const val TAG = "Narou"

    fun getNovel(ncode: String): Novel =
        Jsoup.connect("https://ncode.syosetu.com/$ncode").get().run {
            val title = select(".novel_title").html()
            val writer = select(".novel_writername > a").html().let {
                if (it.isNullOrEmpty()) select(".novel_writername").html()
                    .replace("作者：", "") else it
            }

            Novel(
                ncode,
                title,
                PapagoRequester.request("ja-ko", title),
                writer
            )
        }

    fun getNovelBodies(ncode: String): List<NovelBody> =
        Jsoup.connect("https://ncode.syosetu.com/$ncode").get().run {
            val novelBodyRegex =
                """(?:<div class="chapter_title">\s*(.+?)\s*</div>|<dl class="novel_sublist2">[\s\S]*?<dd class="subtitle">[\s\S]*?<a href="/.+?/(\d+)/">(.+?)</a>)""".toRegex()

            novelBodyRegex.findAll(select(".index_box").html()).map { result ->
                result.groups[1]?.let {
                    return@map NovelBody(it.value, true, 0)
                }
                result.groups[3]?.let { title ->
                    result.groups[2]?.let { index ->
                        return@map NovelBody(title.value, false, index.value.toInt())
                    }
                }

                throw IllegalStateException("Novel body is neither chapter nor episode.")
            }.toList()
        }

    fun getNovelBody(ncode: String, index: Int): NovelBody =
        Jsoup.connect("https://ncode.syosetu.com/$ncode/$index").get().runCatching {
            val body = select(".novel_subtitle").eachText().first()
            val mainTextWrappers =
                select("#novel_honbun > p").eachText().filter { it.trim().isNotEmpty() }.map {
                    TranslationWrapper(
                        it.replace("&lt;", "<")
                            .replace("&gt;", ">")
                            .replace("&amp;", "&")
                    )
                }
            NovelBody(
                body,
                false,
                index,
                mainTextWrappers
            )
        }.getOrThrow()
}
