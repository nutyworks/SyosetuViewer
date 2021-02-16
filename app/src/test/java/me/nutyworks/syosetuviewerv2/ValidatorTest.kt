package me.nutyworks.syosetuviewerv2

import me.nutyworks.syosetuviewerv2.utilities.NcodeValidator
import me.nutyworks.syosetuviewerv2.utilities.ValidatorException
import org.junit.Test

class ValidatorTest {
    @Test
    fun `valid ncode`() {
        NcodeValidator.validate("n1234b")
        NcodeValidator.validate("N4154FL")
    }

    @Test(expected = ValidatorException::class)
    fun `invalid ncode`() {
        NcodeValidator.validate("n12341b")
    }
}
