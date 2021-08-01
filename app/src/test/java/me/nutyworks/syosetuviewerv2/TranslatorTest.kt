package me.nutyworks.syosetuviewerv2

import com.google.common.truth.Truth.assertThat
import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.network.PapagoRequester
import me.nutyworks.syosetuviewerv2.network.bulkTranslator
import org.junit.Test

class TranslatorTest {
    @Test
    fun `translation test`() {
        assertThat(PapagoRequester.request("ja-ko", "リンゴ")).isEqualTo("사과")
    }

    @Test
    fun `bulk translator test`() {

        val wrappers: List<TranslationWrapper> = listOf(
            "おはようございます。",
            "リンゴ"
        ).map { TranslationWrapper(it) }

        bulkTranslator("ja-ko") {
            wrappers.forEach {
                it.original translateTo it::translated
            }
        }.run()

        val results: List<String> = wrappers.map { it.translated }

        assertThat(results).isEqualTo(listOf("안녕하세요", "사과"))
    }
}
