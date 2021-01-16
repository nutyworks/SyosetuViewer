package me.nutyworks.syosetuviewerv2.utilities

import java.lang.Exception

object NcodeValidator {
    private val regex = """n\d{4}[a-z]{1,2}""".toRegex()
    fun validate(ncode: String) {
        if (!ncode.matches(regex)) throw ValidatorException("Ncode does not match with regex $regex")


    }
}

class ValidatorException(override val message: String? = null) : Exception()