package me.nutyworks.syosetuviewerv2.data

data class TranslationWrapper(
    val original: String,
    var translated: String = "",
    var viewType: Int = TRANSLATED
) {
    companion object {
        const val ORIGINAL = 0
        const val TRANSLATED = 1
    }

    val text get() = if (viewType == ORIGINAL) original else translated
}
