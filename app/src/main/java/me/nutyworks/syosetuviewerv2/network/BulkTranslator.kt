package me.nutyworks.syosetuviewerv2.network

import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import kotlin.reflect.KMutableProperty

class BulkTranslator(private val language: String) {
    private val toTranslate = mutableListOf<Pair<String, KMutableProperty<String>>>()

    infix fun String.translateTo(other: KMutableProperty<String>) {
        toTranslate.add(this to other)
    }

    fun wrapper(translationWrapper: TranslationWrapper) {
        toTranslate.add(translationWrapper.original to translationWrapper::translated)
    }

    fun run() {
        if (toTranslate.isEmpty()) return

        val str = toTranslate.joinToString("\n") { it.first }
        var idxStart = 0
        var idxEnd: Int
        var listStartIndex = 0
        while (true) {

            idxEnd =
                if (idxStart + 5000 <= str.length)
                    idxStart + str.slice(idxStart until idxStart + 5000).indexOfLast { it == '\n' }
                else
                    str.length

            str.slice(idxStart until idxEnd).let { slicedStr ->
                PapagoRequester.request(language, slicedStr).split("\n").let { response ->
                    response.forEachIndexed { index, s ->
                        toTranslate[listStartIndex + index].second.setter.call(s)
                    }
                    listStartIndex += response.size
                }
            }

            idxStart = idxEnd + 1

            if (idxStart > str.length)
                break
        }
    }
}

fun bulkTranslator(language: String, bulkTranslator: BulkTranslator.() -> Unit): BulkTranslator {
    return BulkTranslator(language).apply(bulkTranslator)
}
