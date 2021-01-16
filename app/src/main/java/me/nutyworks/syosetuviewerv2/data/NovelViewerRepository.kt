package me.nutyworks.syosetuviewerv2.data

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.network.Narou
import me.nutyworks.syosetuviewerv2.network.bulkTranslator

class NovelViewerRepository {

    companion object {
        private const val TAG = "NovelViewerRepository"
    }

    val novelMainText = MutableLiveData<List<TranslationWrapper>>(listOf())

    suspend fun fetchEpisode(ncode: String, index: Int) {
        Narou.getNovelBody(ncode, index).map { TranslationWrapper(it) }.also { l ->
            bulkTranslator {
                l.forEach {
                    it.original translateTo it::translated
                }
            }.run()
        }.let {
            withContext(Dispatchers.Main) {
                novelMainText.value = it
            }
        }
    }
}