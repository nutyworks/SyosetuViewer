package me.nutyworks.syosetuviewerv2.data

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.nutyworks.syosetuviewer.translator.PapagoRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.network.Narou
import me.nutyworks.syosetuviewerv2.network.bulkTranslator
import me.nutyworks.syosetuviewerv2.utilities.NcodeValidator

class NovelRepository private constructor(
    application: Application
) {

    companion object {
        private const val TAG = "NovelEntityRepository"
        private var instance: NovelRepository? = null

        fun getInstance(): NovelRepository {
            return instance ?: throw IllegalStateException("Instance is null")
        }

        fun getInstance(application: Application): NovelRepository {
            if (instance == null) {
                synchronized(this) {
                    instance = NovelRepository(application)
                }
            }

            return instance ?: throw IllegalStateException("Instance is null")
        }
    }

    val selectedNovelBodies = MutableLiveData<List<NovelBody>>(listOf())
    val novelMainText = MutableLiveData<List<TranslationWrapper>>(listOf())
    private val db = NovelDatabase.getInstance(application)
    private val mNovelEntityDao = db.novelDao()
    val novels = mNovelEntityDao.getAll()

    suspend fun fetchSelectedNovelBodies(ncode: String) {
        Log.i(TAG, "fetchSelectedNovelBodies called with ncode $ncode")
        Narou.getNovelBodies(ncode).let { bodies ->
            bulkTranslator {
                bodies.forEach {
                    it.body translateTo it::translatedBody
                }
            }.run()

            withContext(Dispatchers.Main) {
                selectedNovelBodies.value = bodies
            }
        }
    }

    suspend fun insertNovel(novel: Novel) = mNovelEntityDao.insert(novel)

    suspend fun insertNovel(ncode: String) {
        NcodeValidator.validate(ncode)

        Log.i(TAG, "insertNovel called with ncode $ncode")
        withContext(Dispatchers.IO) {
            Narou.getNovel(ncode).let { novel ->
                Novel(
                    novel.ncode,
                    novel.title,
                    PapagoRequester.request(novel.title),
                    novel.writer
                )
            }.let { novelEntity ->
                insertNovel(novelEntity)
            }
        }
    }

    suspend fun deleteNovel(novel: Novel) = mNovelEntityDao.delete(novel)
    suspend fun deleteAll() = mNovelEntityDao.deleteAll()

    suspend fun markAsRead(novel: Novel, index: Int) {
        if (novel.readIndexes.split(",").contains(index.toString())) return

        novel.readIndexes += "$index,"
        insertNovel(novel)
    }

    suspend fun markAsRead(ncode: String, index: Int) {
        val novel = novels.value?.find { it.ncode == ncode } ?: return
        markAsRead(novel, index)
    }

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