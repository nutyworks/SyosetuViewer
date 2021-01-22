package me.nutyworks.syosetuviewerv2

import me.nutyworks.syosetuviewerv2.utilities.NcodeValidator
import org.junit.Test

class ValidatorTest {
    @Test
    fun `Vaildator test`() {
        NcodeValidator.validate("n1234b")
        NcodeValidator.validate("N4154FL")
    }
}