package me.nutyworks.syosetuviewerv2.data

data class NovelBody(
    val title: TranslationWrapper,
    val isChapter: Boolean,
    val index: Int,
    val mainTextWrappers: List<IMainTextWrapper>? = null,
)

interface IMainTextWrapper
