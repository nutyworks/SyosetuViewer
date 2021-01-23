package me.nutyworks.syosetuviewerv2.ui

import android.content.Intent
import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import me.nutyworks.syosetuviewerv2.SearchResultActivity
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.data.YomouSearchResult

class SearchResultViewModel : ViewModel() {

    private val mRepository = NovelRepository.getInstance()
    val searchResults: ObservableField<List<YomouSearchResult>> = mRepository.searchResults

    fun init(intent: Intent) {
        intent.let {
            val wordInclude = it.getStringExtra(SearchResultActivity.INTENT_INCLUDE_WORDS)!!
            GlobalScope.launch {
                mRepository.searchNovel(wordInclude)
            }
        }
    }
}
