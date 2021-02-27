package me.nutyworks.syosetuviewerv2.novel.kakuyomu

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelFactory
import me.nutyworks.syosetuviewerv2.novel.NovelStatus
import org.jsoup.Jsoup
import java.lang.IllegalStateException

class KakuyomuNovelFactory : NovelFactory() {
    override fun getProvidedNovel(identifier: String): Novel =
        Jsoup.connect("https://kakuyomu.jp/works/$identifier").get().run {
            KakuyomuNovel(
                select("#workTitle > a").attr("href").replace("/works/", ""),
                select("#workTitle > a").text(),
                select("#workAuthor-activityName > a").text(),
                when (val status = select("dl.widget-credit:nth-child(1) > dd:nth-child(2)").text()) {
                    "連載中" -> NovelStatus.IN_PROGRESS
                    "完結済" -> NovelStatus.FINISHED
                    else -> throw IllegalStateException("Inappropriate novel status: $status. Expected 連載中 or 完結済.")
                },
                select("#introduction").html().replace("<br>", "\n"),
                select("dd > span[itemprop=keywords] > a").eachText(),
                select("dd[itemprop=genre] > a").text(),
                """<dd>[\n\r\s]*?([\d,]+)文字[\n\r\s]*?</dd>""".toRegex()
                    .find(select("#workInformationList").html())?.groupValues?.get(1)
                    ?.replace(",", "")?.toInt()
                    ?: throw IllegalStateException("Failed to get number of characters."),
            )
        }
}
