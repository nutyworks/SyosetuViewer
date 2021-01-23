package me.nutyworks.syosetuviewerv2.network

import kotlin.reflect.KMutableProperty

class BulkTranslator {
    private val toTranslate = mutableListOf<Pair<String, KMutableProperty<String>>>()

    infix fun String.translateTo(other: KMutableProperty<String>) {
        toTranslate.add(this to other)
    }

    fun run() {
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
                PapagoRequester.request(slicedStr).split("\n").let { response ->
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

fun bulkTranslator(bulkTranslator: BulkTranslator.() -> Unit): BulkTranslator {
    return BulkTranslator().apply(bulkTranslator)
}

object BulkStringTranslator {

    fun translate(str: String): String {
        var translated = ""

        var idxStart = 0
        var idxEnd: Int
        while (true) {

            idxEnd =
                if (idxStart + 5000 < str.length) {
                    idxStart + str.slice(idxStart until idxStart + 5000)
                        .indexOfLast { it == '\n' }
                } else str.length

            str.slice(idxStart until idxEnd).let { slicedStr ->
                translated += PapagoRequester.request(slicedStr) + "\n"
            }

            idxStart = idxEnd + 1

            if (idxStart > str.length)
                break
        }

        return translated
    }
}
