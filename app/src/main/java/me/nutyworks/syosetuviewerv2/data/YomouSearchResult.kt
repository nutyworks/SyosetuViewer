package me.nutyworks.syosetuviewerv2.data

data class YomouSearchResult(
    val title: TranslationWrapper,
    val writer: String,
    val ncode: String,
    val status: String,
    val episodes: Int,
    val description: TranslationWrapper,
    val genre: TranslationWrapper,
    val keywords: List<TranslationWrapper>
)
