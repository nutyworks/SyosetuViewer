package me.nutyworks.syosetuviewerv2.utilities

object NcodeValidator {
    private val regex =
        """[Nn]\d{4}[A-Za-z]{1,2}""".toRegex()
    fun validate(ncode: String) {
        if (!ncode.matches(regex)) throw ValidatorException("Ncode does not match with regex $regex")
    }
}

class ValidatorException(override val message: String? = null) : Exception()
