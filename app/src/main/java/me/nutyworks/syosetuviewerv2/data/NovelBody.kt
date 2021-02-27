package me.nutyworks.syosetuviewerv2.data

@Deprecated("old")
data class NovelBody(
    val title: TranslationWrapper,
    val isChapter: Boolean,
    val index: Int,
    val mainTextWrappers: List<IMainTextWrapper>? = null,
)

@Deprecated("old")
interface IMainTextWrapper
