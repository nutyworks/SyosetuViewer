package me.nutyworks.syosetuviewerv2.novel

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "novels")
data class NovelEntity(
    @PrimaryKey val identifier: String,
    val provider: NovelProvider,
) {
    fun toNovel() {
        when (provider) {
            NovelProvider.NAROU ->
                NovelFactory.getNovel("https://ncode.syosetu.com/$identifier")
            NovelProvider.KAKUYOMU ->
                NovelFactory.getNovel("https://kakuyomu.jp/works/$identifier")
        }
    }
}