package me.nutyworks.syosetuviewerv2.data

data class TranslationWrapper(
    val original: String,
    var translated: String = original,
    var viewType: Int = TRANSLATED
) {
    companion object {
        const val ORIGINAL = 0
        const val TRANSLATED = 1
    }

    val text get() = if (viewType == ORIGINAL) original else translated

    fun toggleViewType() {
        viewType = when (viewType) {
            ORIGINAL -> TRANSLATED
            TRANSLATED -> ORIGINAL
            else -> TRANSLATED
        }
    }
}
