package me.nutyworks.syosetuviewerv2.ui

import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import me.nutyworks.syosetuviewerv2.SearchResultActivity
import me.nutyworks.syosetuviewerv2.adapter.SearchResultAdapter
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.data.YomouSearchResult
import me.nutyworks.syosetuviewerv2.network.Yomou
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent

class SearchResultViewModel : ViewModel() {

    companion object {
        private const val TAG = "SearchResultViewModel"
    }

    private val mRepository = NovelRepository.getInstance()
    val searchResults: LiveData<List<YomouSearchResult>> = mRepository.searchResults
    val adapter = SearchResultAdapter(this)
    val searchResultsUpdateEvent = mRepository.searchResultsInsertedEvent

    private val mViewModelScope = CoroutineScope(Job() + Dispatchers.Main)
    private var mSearchJob: Deferred<Unit>? = null

    private lateinit var wordInclude: String

    val resultsRecyclerViewIsVisible = ObservableBoolean(false)
    val loadingProgressBarIsVisible = ObservableBoolean(true)
    val extraLoadingProgressBarIsVisible = mRepository.isExtraLoading

    var page = 1

    val addNovelEvent = SingleLiveEvent<Void>()

    val onReachEnd: () -> Unit = {
        if (!mRepository.isExtraLoading.get()) {
            mSearchJob = mViewModelScope.async {
                mRepository.searchNovel(wordInclude, page++)
            }
        }
    }

    fun init(intent: Intent) {
        with(intent) {
            wordInclude = getStringExtra(SearchResultActivity.INTENT_INCLUDE_WORDS)!!
        }
        mSearchJob = mViewModelScope.async {
            mRepository.searchNovel(wordInclude, page++)
        }
    }

    fun getNovelWriterAndStatus(position: Int): String {
        return searchResults.value!![position].run {
            "$writer · $status · $episodes episode${if (episodes == 1) "" else "s"}"
        }
    }

    fun getTranslatedKeywords(position: Int): String =
        searchResults.value!![position].keywords.joinToString(" ") { it.translated }.let {
            if (it.isEmpty()) "No keywords" else it
        }

    fun notifyListAdapterForItemInsert() {
        Log.i(TAG, "notifyListAdapterForUpdate searchResults = ${searchResults.value}")
        adapter.notifyItemRangeInserted((page - 1) * 20, 20)
    }

    fun addNovel(position: Int) {
        addNovelEvent.call()
        mViewModelScope.launch {
            mRepository.insertNovel(searchResults.value!![position].ncode)
        }
    }

    fun resetSearchResult() {
        mRepository.resetSearchResult()
        mSearchJob?.cancel()
    }
}
