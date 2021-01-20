package me.nutyworks.syosetuviewerv2.network

import android.util.Log
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

            Log.d("BulkSTranslator", "translate request [$idxStart, $idxEnd)")
            str.slice(idxStart until idxEnd).let { slicedStr ->
                Log.d("BulkSTranslator", "request string last 30 chars ${slicedStr.takeLast(30)}")
                Log.d("BulkSTranslator", "request length ${slicedStr.length}")
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
        Log.d("BulkSTranslator", "got translate request of string length ${str.length}")
        var translated = ""

        var idxStart = 0
        var idxEnd: Int
        while (true) {

            idxEnd =
                if (idxStart + 5000 < str.length) {
                    idxStart + str.slice(idxStart until idxStart + 5000)
                        .indexOfLast { it == '\n' }
                } else str.length

            Log.d("BulkSTranslator", "translate request [$idxStart, $idxEnd)")
            str.slice(idxStart until idxEnd).let { slicedStr ->
                Log.d("BulkSTranslator", "request string last 30 chars ${slicedStr.takeLast(30)}")
                Log.d("BulkSTranslator", "request length ${slicedStr.length}")
                translated += PapagoRequester.request(slicedStr) + "\n"
            }

            idxStart = idxEnd + 1

            if (idxStart > str.length)
                break
        }

        return translated
    }
}