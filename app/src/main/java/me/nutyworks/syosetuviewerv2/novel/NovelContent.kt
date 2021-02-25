package me.nutyworks.syosetuviewerv2.novel

interface NovelContent {
    val parent: NovelContent?
    val title: String
}
