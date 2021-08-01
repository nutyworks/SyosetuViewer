package me.nutyworks.syosetuviewerv2.data

data class TranslationWrapper(
    val original: String,
    var translated: String = original,
    var viewType: Int = TRANSLATED,
) : IMainTextWrapper {
    companion object {
        const val ORIGINAL = 0
        const val TRANSLATED = 1
        private val mRepository by lazy { NovelRepository.getInstance() }
    }

    val text
        get() =
            (if (viewType == ORIGINAL) original else translated).let {
                if (mRepository.wordWrap.get()) it else it.replace(" ", "\u00a0")
            }

    fun toggleViewType() {
        viewType = when (viewType) {
            ORIGINAL -> TRANSLATED
            TRANSLATED -> ORIGINAL
            else -> TRANSLATED
        }
    }
}

fun String.wrap(): TranslationWrapper = TranslationWrapper(this)
