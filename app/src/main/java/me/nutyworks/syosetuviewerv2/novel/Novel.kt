package me.nutyworks.syosetuviewerv2.novel

interface Novel {
    val identifier: String
    val title: String
    val author: String
    val status: NovelStatus
    val description: String
    val keywords: List<String>
    val genre: String
    val nCharacters: Int

    fun getContents(): List<NovelContent>
}
