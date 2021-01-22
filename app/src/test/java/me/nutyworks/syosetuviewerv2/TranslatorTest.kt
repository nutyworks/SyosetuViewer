package me.nutyworks.syosetuviewerv2

import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.network.PapagoRequester
import me.nutyworks.syosetuviewerv2.network.bulkTranslator
import org.junit.Assert.assertEquals
import org.junit.Test

class TranslatorTest {
    @Test
    fun `PapagoRequester translation test`() {
        assertEquals(PapagoRequester.request("リンゴ"), "사과")
    }

    @Test
    fun `PapagoRequester BulkTranslator test`() {

        val wrappers: List<TranslationWrapper> = listOf(
            "おはようございます。",
            "リンゴ"
        ).map { TranslationWrapper(it) }

        bulkTranslator {
            wrappers.forEach {
                it.original translateTo it::translated
            }
        }.run()

        val results: List<String> = wrappers.map { it.translated }

        assertEquals(results, listOf("안녕하세요", "사과"))
    }
}
