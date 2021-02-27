package me.nutyworks.syosetuviewerv2.novel

import java.lang.IllegalArgumentException

abstract class NovelFactory {

    companion object {
        fun getNovel(uri: String): Novel {
            val narouRegex =
                """https?://ncode.syosetu.com/(n\d{4}[a-z]{1,2})/?(?:\d+/)?""".toRegex()
            val kakuyomuRegex =
                """https?://kakuyomu.jp/works/(\d+)/?(?:episodes/\d+)?""".toRegex()

            val (provider: NovelProvider, identifier: String) = run {
                narouRegex.matchEntire(uri)?.destructured?.let { (ncode) ->
                    return@run Pair(NovelProvider.NAROU, ncode)
                }
                kakuyomuRegex.matchEntire(uri)?.destructured?.let { (workId) ->
                    return@run Pair(NovelProvider.KAKUYOMU, workId)
                }
                throw IllegalArgumentException("Uri $uri is invalid")
            }

            return getNovel(provider, identifier)
        }

        private fun getNovel(provider: NovelProvider, identifier: String): Novel {
            return provider.factory.getProvidedNovel(identifier)
        }
    }

    protected abstract fun getProvidedNovel(identifier: String): Novel
}
