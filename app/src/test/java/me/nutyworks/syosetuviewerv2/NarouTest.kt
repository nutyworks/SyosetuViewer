package me.nutyworks.syosetuviewerv2

import com.google.common.truth.Truth.assertThat
import me.nutyworks.syosetuviewerv2.data.ImageWrapper
import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.network.Narou
import org.jsoup.HttpStatusException
import org.junit.Test

class NarouTest {

    @Test
    fun `get novel with valid ncode non-r18`() {
        Narou.getNovel("n6316bn", false).let {
            assertThat(it.ncode).isEqualTo("n6316bn")
            assertThat(it.title).isEqualTo("転生したらスライムだった件")
            assertThat(it.writer).isEqualTo("伏瀬")
        }
    }

    @Test(expected = IllegalAccessException::class)
    fun `get novel with valid ncode r18 not over 18`() {
        Narou.getNovel("n9504fy", false)
    }

    @Test
    fun `get novel with valid ncode r18 over 18`() {
        Narou.getNovel("n9504fy", true)
    }

    @Test(expected = HttpStatusException::class)
    fun `get novel with invalid ncode`() {
        Narou.getNovel("n1123da", false)
    }

    @Test
    fun `get novel bodies with valid ncode`() {
        Narou.getNovelBodies("n6316bn").let {
            assertThat(it.size).isEqualTo(316)
            assertThat(it[0].isChapter).isTrue()
            assertThat(it[0].title.original).isEqualTo("序章")
        }
    }

    @Test(expected = HttpStatusException::class)
    fun `get novel bodies with invalid ncode`() {
        Narou.getNovelBodies("n1123da")
    }

    @Test
    fun `get novel body with valid ncode and valid index`() {
        Narou.getNovelBody("n6316bn", 1).let {
            assertThat(it.title.original).isEqualTo("死亡～そして転生～")
            assertThat(it.isChapter).isEqualTo(false)
            assertThat(it.index).isEqualTo(1)
            assertThat(it.mainTextWrappers).containsAtLeast(
                TranslationWrapper("　何ということもない普通の人生。"),
                TranslationWrapper("　それが、三上悟が、この世で思った最後の言葉だった。")
            )
        }
    }

    @Test
    fun `get novel body with valid ncode and valid index with illustration`() {
        Narou.getNovelBody("n6316bn", 87).let {
            assertThat(it.mainTextWrappers).containsAtLeast(
                ImageWrapper(
                    "//8371.mitemin.net/userpageimage/viewimagebig/icode/i76003/",
                    "挿絵(By みてみん)"
                ),
                TranslationWrapper("髪：白金　瞳：金　肌：褐色")
            )
        }
    }

    @Test(expected = HttpStatusException::class)
    fun `get novel body with valid ncode and invalid index`() {
        Narou.getNovelBody("n6316bn", 0)
    }

    @Test(expected = HttpStatusException::class)
    fun `get novel body with invalid ncode`() {
        Narou.getNovelBody("n1123da", 1)
    }
}
