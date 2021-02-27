package me.nutyworks.syosetuviewerv2.novel

sealed class NovelEpisodeComponent {
    data class Text(val text: String) : NovelEpisodeComponent()
    data class Image(val uri: String, val alt: String) : NovelEpisodeComponent()
    object Blank : NovelEpisodeComponent()
}
