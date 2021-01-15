package me.nutyworks.syosetuviewerv2

import narou4j.Narou
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class NarouTest {
    @Test
    fun `Narou get novel list and body by ncode`() {
        assertEquals(Narou().getNovel("n4154fl").title,
            "魔力チートな魔女になりました～創造魔法で気ままな異世界生活～")
        assertTrue(Narou().getNovelBody("n4154fl", 1).body
            .contains("私は、緑豊かな森の中に屋敷を建て、そこで生活している。"))
    }
}