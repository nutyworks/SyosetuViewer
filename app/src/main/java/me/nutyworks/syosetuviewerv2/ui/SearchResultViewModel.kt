package me.nutyworks.syosetuviewerv2.ui

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.nutyworks.syosetuviewerv2.SearchResultActivity
import me.nutyworks.syosetuviewerv2.adapter.SearchResultAdapter
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.data.YomouSearchResult

class SearchResultViewModel : ViewModel() {

    companion object {
        private const val TAG = "SearchResultViewModel"
    }

    private val mRepository = NovelRepository.getInstance()
    val searchResults: LiveData<List<YomouSearchResult>> = mRepository.searchResults
    val adapter = SearchResultAdapter(this)

    fun init(intent: Intent) {
        intent.let {
            val wordInclude = it.getStringExtra(SearchResultActivity.INTENT_INCLUDE_WORDS)!!
            GlobalScope.launch {
                mRepository.searchNovel(wordInclude)
            }
        }
    }

    fun getNovelWriterAndStatus(position: Int): String {
        return searchResults.value!![position].run {
            "$writer · $status · $episodes episode${if (episodes == 1) "" else "s"}"
        }
    }

    fun getTranslatedKeywords(position: Int): String {
        return searchResults.value!![position].keywords.joinToString(" ") { it.translated }
    }

    fun notifyListAdapterForUpdate() {
        Log.i(TAG, "notifyListAdapterForUpdate searchResults = $searchResults")
        adapter.notifyDataSetChanged()
    }
}
