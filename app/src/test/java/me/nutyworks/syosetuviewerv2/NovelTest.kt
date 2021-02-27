package me.nutyworks.syosetuviewerv2

import me.nutyworks.syosetuviewerv2.novel.NovelStatus
import me.nutyworks.syosetuviewerv2.novel.kakuyomu.KakuyomuNovel
import me.nutyworks.syosetuviewerv2.novel.narou.NarouNovel
import org.junit.Test

class NovelTest {
    @Test
    fun narouTest() {
        NarouNovel("n4154fl", "", "", NovelStatus.SHORT, "", listOf(), "", 0, false).getContents()
            .also(::println)
    }

    @Test
    fun kakuyomuTest() {
        KakuyomuNovel(
            "1177354054918994203",
            "",
            "",
            NovelStatus.SHORT,
            "",
            listOf(),
            "",
            0
        ).getContents()
            .map { it.parent to it.name }
            .forEach(::println)
    }
}
