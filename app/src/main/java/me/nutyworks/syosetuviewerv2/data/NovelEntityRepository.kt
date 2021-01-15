package me.nutyworks.syosetuviewerv2.data

import android.app.Application
import android.util.Log
import com.nutyworks.syosetuviewer.translator.PapagoRequester
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.network.Narou
import java.lang.IllegalArgumentException

class NovelEntityRepository constructor(
    application: Application
) {

    companion object {
        private const val TAG = "NovelEntityRepository"
    }

    private val db = NovelDatabase.getInstance(application)
    private val mNovelEntityDao = db.novelDao()
    val novels = mNovelEntityDao.getAll()

    suspend fun insertNovel(novel: Novel) = mNovelEntityDao.insert(novel)

    suspend fun insertNovel(ncode: String) {
        Log.i(TAG, "insertNovel called with ncode $ncode")
        withContext(Dispatchers.IO) {
            Narou.getNovel(ncode).let { novel ->
                Novel(novel.ncode,
                            novel.title,
                            PapagoRequester.request(novel.title),
                            novel.writer)
            }.let { novelEntity ->
                insertNovel(novelEntity)
            }
        }
    }

    suspend fun deleteNovel(novel: Novel) = mNovelEntityDao.delete(novel)
    suspend fun deleteAll() = mNovelEntityDao.deleteAll()
}