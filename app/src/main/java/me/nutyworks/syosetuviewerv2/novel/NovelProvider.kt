package me.nutyworks.syosetuviewerv2.novel

import me.nutyworks.syosetuviewerv2.novel.kakuyomu.KakuyomuNovelFactory
import me.nutyworks.syosetuviewerv2.novel.narou.NarouNovelFactory

enum class NovelProvider(val factory: NovelFactory) {
    NAROU(NarouNovelFactory()),
    KAKUYOMU(KakuyomuNovelFactory()),
}
