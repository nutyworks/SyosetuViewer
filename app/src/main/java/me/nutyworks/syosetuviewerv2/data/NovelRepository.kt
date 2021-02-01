package me.nutyworks.syosetuviewerv2.data

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.core.content.edit
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableFloat
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.network.Narou
import me.nutyworks.syosetuviewerv2.network.Yomou
import me.nutyworks.syosetuviewerv2.network.bulkTranslator
import me.nutyworks.syosetuviewerv2.utilities.NcodeValidator
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import java.net.ConnectException

class NovelRepository private constructor(
    application: Application
) {

    companion object {
        private const val TAG = "NovelEntityRepository"
        private const val PREF_NAMESPACE_VIEWER = "PREF_NAMESPACE_VIEWER"
        private const val PREF_TEXT_SIZE = "PREF_TEXT_SIZE"
        private const val PREF_PARAGRAPH_SPACING = "PREF_PARAGRAPH_SPACING"
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
    val novelBody = ObservableField<NovelBody>()
    private val db = NovelDatabase.getInstance(application)
    private val mNovelEntityDao = db.novelDao()
    val novels = mNovelEntityDao.getAll()

    val searchResults = MutableLiveData<List<YomouSearchResult>>(listOf())
    val searchResultsInsertedEvent = SingleLiveEvent<Void>()
    val isExtraLoading = ObservableBoolean(false)
    val snackbarText = SingleLiveEvent<String>()

    private val mSharedPreferences =
        application.getSharedPreferences(PREF_NAMESPACE_VIEWER, Context.MODE_PRIVATE)

    val textSize = ObservableFloat(mSharedPreferences.getFloat(PREF_TEXT_SIZE, 16f))
    val paragraphSpacing = ObservableFloat(mSharedPreferences.getFloat(PREF_PARAGRAPH_SPACING, 5f))

    suspend fun fetchSelectedNovelBodies(ncode: String) {
        Log.i(TAG, "fetchSelectedNovelBodies called with ncode $ncode")

        val translatedNovelBodies = withContext(Dispatchers.IO) {
            val novelBodies = Narou.getNovelBodies(ncode)
            bulkTranslator("ja-ko") {
                novelBodies.forEach {
                    wrapper(it.title)
                }
            }.run().runUntranslated()

            novelBodies
        }

        selectedNovelBodies.postValue(translatedNovelBodies)
    }

    suspend fun insertNovel(novel: Novel) = mNovelEntityDao.insert(novel)

    suspend fun insertNovel(ncode: String) {
        NcodeValidator.validate(ncode)

        Log.i(TAG, "insertNovel called with ncode $ncode")
        try {
            withContext(Dispatchers.IO) {
                val novelEntity = Narou.getNovel(ncode)
                insertNovel(novelEntity)
            }
        } catch (e: ConnectException) {
            Log.e(TAG, "Error occurred while inserting novel", e)
            snackbarText.value = e.message
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
        val translatedNovelBody = withContext(Dispatchers.IO) {
            val novelBody = Narou.getNovelBody(ncode, index)
            bulkTranslator("ja-ko") {
                novelBody.mainTextWrappers?.forEach {
                    (it as? TranslationWrapper)?.let { t -> wrapper(t) }
                }
                wrapper(novelBody.title)
            }.run().runUntranslated()

            novelBody
        }
        novelBody.set(translatedNovelBody)
    }

    suspend fun searchNovel(requirements: SearchRequirements, page: Int = 1) {
        Log.i(TAG, requirements.toString())
        isExtraLoading.set(true)

        requirements.run {
            val translatedResults = withContext(Dispatchers.IO) {
                val (wordIncludeTranslated, wordExcludeTranslated) = run {
                    val includeWrapper = includeWords.split(" ").map {
                        it.wrap()
                    }
                    val excludeWrapper = excludeWords.split(" ").map {
                        it.wrap()
                    }
                    bulkTranslator("ko-ja") {
                        includeWrapper.forEach { wrapper(it) }
                        excludeWrapper.forEach { wrapper(it) }
                    }.run().runUntranslated()

                    includeWrapper.joinToString(" ") { it.translated } to
                        excludeWrapper.joinToString(" ") { it.translated }
                }

                val results =
                    Yomou.search(
                        wordInclude = wordIncludeTranslated,
                        wordExclude = wordExcludeTranslated,
                        genres = genres.toList(),
                        page = page,
                        types = requireType,
                        minTime = minTime,
                        maxTime = maxTime,
                        minLen = minLen,
                        maxLen = maxLen,
                        minGlobalPoint = minGlobalPoint,
                        maxGlobalPoint = maxGlobalPoint,
                        minLastUp = minLastUp,
                        maxLastUp = maxLastUp,
                        minFirstUp = minFirstUp,
                        maxFirstUp = maxFirstUp,
                        order = Yomou.Order.orderByList[orderBy],
                    )

                bulkTranslator("ja-ko") {
                    results.forEach { result ->
                        wrapper(result.title)
                        wrapper(result.genre)
                        result.keywords.forEach { keyword ->
                            wrapper(keyword)
                        }
                    }
                }.run().runUntranslated()

                results
            }

            isExtraLoading.set(false)
            searchResults.value = searchResults.value?.plus(translatedResults)
            searchResultsInsertedEvent.call()
        }
    }

    fun resetSearchResult() {
        searchResults.value = listOf()
    }

    fun setTextSize(textSize: Float) {
        this.textSize.set(textSize)
        mSharedPreferences.edit {
            putFloat(PREF_TEXT_SIZE, textSize)
        }
    }

    fun setParagraphSpacing(paragraphSpacing: Float) {
        this.paragraphSpacing.set(paragraphSpacing)
        mSharedPreferences.edit {
            putFloat(PREF_PARAGRAPH_SPACING, paragraphSpacing)
        }
    }
}
