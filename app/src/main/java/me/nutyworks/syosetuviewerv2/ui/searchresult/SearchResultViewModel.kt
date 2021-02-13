package me.nutyworks.syosetuviewerv2.ui.searchresult

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
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.data.SearchRequirements
import me.nutyworks.syosetuviewerv2.data.YomouSearchResult
import me.nutyworks.syosetuviewerv2.network.Yomou

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

    private lateinit var requirements: SearchRequirements

    val resultsRecyclerViewIsVisible = ObservableBoolean(false)
    val loadingProgressBarIsVisible = ObservableBoolean(true)
    val extraLoadingProgressBarIsVisible = mRepository.isExtraLoading

    var page = 1

    val snackbarText = mRepository.snackbarText

    val onReachEnd: () -> Unit = {
        if (!mRepository.isExtraLoading.get()) {
            searchNextPage()
        }
    }

    private fun searchNextPage() {
        mSearchJob = mViewModelScope.async {
            mRepository.searchNovel(requirements, page++)
        }
    }

    fun init(intent: Intent) {
        with(intent) {
            requirements =
                getSerializableExtra(SearchResultActivity.INTENT_SEARCH_REQUIREMENTS) as SearchRequirements

            Log.i(TAG, requirements.toString())
        }
        searchNextPage()
    }

    fun getWriterAndNcode(position: Int): String {
        return searchResults.value!![position].run {
            "$writer · $ncode"
        }
    }

    fun getNovelInfo(position: Int): String {
        return searchResults.value!![position].run {
            "${genre.translated} · ${Yomou.Type.typeToString(status)} · $episodes episode${if (episodes == 1) "" else "s"}"
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
        mViewModelScope.launch {
            snackbarText.postValue("Adding selected novel to your library...")
            mRepository.insertNovel(searchResults.value!![position].ncode)
            snackbarText.postValue("Added selected novel to your library!")
        }
    }

    fun resetSearchResult() {
        mRepository.resetSearchResult()
        mSearchJob?.cancel()
    }
}
