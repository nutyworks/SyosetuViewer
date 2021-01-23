package me.nutyworks.syosetuviewerv2

import me.nutyworks.syosetuviewerv2.network.Yomou
import org.junit.Test

class YomouTest {
    @Test
    fun `Yomou search test`() {
        Yomou.search(
            genres = listOf(Yomou.Genre.HIGH_FANTASY)
        )
    }
}
