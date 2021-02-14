package me.nutyworks.syosetuviewerv2

import me.nutyworks.syosetuviewerv2.utilities.NcodeValidator
import me.nutyworks.syosetuviewerv2.utilities.ValidatorException
import org.junit.Test

class ValidatorTest {
    @Test
    fun `validator test returns success`() {
        NcodeValidator.validate("n1234b")
        NcodeValidator.validate("N4154FL")
    }

    @Test(expected = ValidatorException::class)
    fun `validator test throws ValidatorException`() {
        NcodeValidator.validate("n12341b")
    }
}
