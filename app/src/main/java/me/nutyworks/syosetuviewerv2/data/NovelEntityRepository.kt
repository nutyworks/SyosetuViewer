package me.nutyworks.syosetuviewerv2.data

import android.app.Application
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import narou4j.Narou

class NovelEntityRepository constructor(
    application: Application
) {

    companion object {
        private const val TAG = "NovelEntityRepository"
    }

    private val db = NovelDatabase.getInstance(application)
    private val mNovelEntityDao = db.novelEntityDao()
    private val mNarou = Narou()
    val novels = mNovelEntityDao.getAll()

    suspend fun insertNovel(novel: NovelEntity) = mNovelEntityDao.insert(novel)

    suspend fun insertNovel(ncode: String) {
        Log.i(TAG, "insertNovel called with ncode $ncode")
        withContext(Dispatchers.IO) {
            mNarou.getNovel(ncode).let { novel ->
                NovelEntity(ncode, novel.title, "asdf", novel.writer)
            }.let { novelEntity ->
                insertNovel(novelEntity)
            }
        }
    }

    suspend fun deleteNovel(novel: NovelEntity) = mNovelEntityDao.delete(novel)
    suspend fun deleteAll() = mNovelEntityDao.deleteAll()
}