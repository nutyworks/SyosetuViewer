package me.nutyworks.syosetuviewerv2.novel.narou

import me.nutyworks.syosetuviewerv2.novel.Novel
import me.nutyworks.syosetuviewerv2.novel.NovelFactory
import me.nutyworks.syosetuviewerv2.novel.NovelStatus
import org.jsoup.Jsoup

class NarouNovelFactory : NovelFactory() {
    override fun getProvidedNovel(identifier: String): Novel =
        Jsoup.connect("https://ncode.syosetu.com/novelview/infotop/ncode/$identifier/")
            .cookie("over18", "yes").get().run {
                val status = when (val status = select("#pre_info > span:nth-child(1)").text()) {
                    "短編" -> NovelStatus.SHORT
                    "連載中" -> NovelStatus.IN_PROGRESS
                    "完結済" -> NovelStatus.FINISHED
                    else -> throw IllegalStateException("Inappropriate novel status: $status. Expected 短編, 連載中, or 完結済.")
                }

                NarouNovel(
                    select("#ncode").text(),
                    select("h1 > a").text(),
                    select("#noveltable1 > tbody > tr:nth-child(2) > td:nth-child(2) > a").text(),
                    status,
                    select("#noveltable1 > tbody > tr:nth-child(1) > td:nth-child(2)").html()
                        .replace("<br>", "\n"),
                    select("#noveltable1 > tbody > tr:nth-child(3) > td:nth-child(2)").text()
                        .split(" ").filter(String::isNotEmpty),
                    select("#noveltable1 > tbody > tr:nth-child(4) > td:nth-child(2)").text(),
                    select("#noveltable2 > tbody > tr:nth-child(${if (status == NovelStatus.SHORT) 9 else 10}) > td:nth-child(2)").text()
                        .replace(",", "").replace("文字", "").toInt(),
                    select(".contents1").html().contains("＜R18＞"),
                )
            }
}
