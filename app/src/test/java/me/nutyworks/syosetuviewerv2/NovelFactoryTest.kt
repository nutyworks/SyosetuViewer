package me.nutyworks.syosetuviewerv2

import com.google.common.truth.Truth.assertThat
import me.nutyworks.syosetuviewerv2.novel.NovelEpisode
import me.nutyworks.syosetuviewerv2.novel.NovelFactory
import me.nutyworks.syosetuviewerv2.novel.NovelStatus
import me.nutyworks.syosetuviewerv2.novel.kakuyomu.KakuyomuNovel
import me.nutyworks.syosetuviewerv2.novel.narou.NarouNovel
import org.junit.Test

class NovelFactoryTest {

    // TODO fix whitespace prefixes

    @Test
    fun narouTest() {
        assertThat(NovelFactory.getNovel("https://ncode.syosetu.com/n4154fl"))
            .isEqualTo(
                NarouNovel(
                    "N4154FL",
                    "魔力チートな魔女になりました～創造魔法で気ままな異世界生活～",
                    "アロハ座長",
                    NovelStatus.IN_PROGRESS,
                    """　女神・リリエルと名乗る異世界の神に【創造魔法】のユニークスキルを渡されて、名なしの主人公は、異世界に転生する。
　前世の日本の知識はあるけど、具体的に思い出せず――チセと名乗ることにした少女は、【創造魔法】で少ない魔力を増やすために魔力が増える【不思議な木の実】を創り出し、それを食べて増えた魔力でまた創造。
　膨大な魔力を得た少女は、老化が止まり、いつしか不老に。
　不老な創造の魔女・チセは、寿命も老いもないお供のゴーレム娘のテトを連れて、冒険者となって、自身が作り上げた安住の地を拠点に異世界の各地で旅をする。

※ころころタイトルを変えてすみません。引き続きよろしくお願いします。
2019年6月15日、日間ファンタジー異世界転生/転移ランキング1位
2021年1月30日、GCノベルズ様より4巻発売。
2021年3月8日、ガンガンONLINEにてコミカライズ連載開始。 """,
                    listOf(
                        "残酷な描写あり",
                        "異世界転生",
                        "ファンタジー",
                        "異世界",
                        "チート",
                        "女主人公",
                        "創造魔法",
                        "不老の魔女",
                        "ロリババァ予備軍",
                        "ゴーレム娘"
                    ),
                    "ハイファンタジー〔ファンタジー〕",
                    610566,
                    false
                )
            )
    }

    @Test
    fun narouEpisodeTest() {
        (NovelFactory.getNovel("https://ncode.syosetu.com/n5839gu/").getContents()[4] as NovelEpisode).getMainText().forEach(::println)
    }

    @Test
    fun kakuyomuTest() {
        assertThat(NovelFactory.getNovel("https://kakuyomu.jp/works/16816452218249495335"))
            .isEqualTo(
                KakuyomuNovel(
                    "16816452218249495335",
                    "邪神無双　～邪神が黒い笑顔で人助けを始めたようです～",
                    "九頭七尾",
                    NovelStatus.IN_PROGRESS,
                    """異世界に転生したレイジは、なぜか前世の記憶が曖昧になっていた。
そして天使から衝撃的な事実を告げられる。
「君さ、転生を担当した女神様を殺しちゃったんだよねー」
「はい？」
女神を殺したことで、人間でありながら神の力を得てしまったレイジ（ただし邪神）。
それは信者が増えれば増えるほど強くなるという、まさしく神チートな能力だった。
内心では邪神らしい黒い笑みを浮かべつつも、温厚篤実な善人を演じて仲間や友人たちの好感度（もとい信仰度）を上げていくレイジ。
やがて膨大な量の経験値と熟練値（スキルポイント）が入ってくるようになっていて……。 """,
                    listOf(
                        "俺Tueee",
                        "チート",
                        "転生",
                    ),
                    "異世界ファンタジー",
                    113861
                )
            )
    }

    @Test
    fun kakuyomuEpisodeTest1() {
        (NovelFactory.getNovel("https://kakuyomu.jp/works/1177354054893198586").getContents()[4] as NovelEpisode).getMainText().forEach(::println)
    }

    @Test
    fun kakuyomuEpisodeTest2() {
        (NovelFactory.getNovel("https://kakuyomu.jp/works/1177354054992481156").getContents()[0] as NovelEpisode).getMainText().forEach(::println)
    }
}
