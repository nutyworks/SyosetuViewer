package me.nutyworks.syosetuviewerv2.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent

class SearchViewModel : ViewModel() {

    val startSearchResultActivityEvent = SingleLiveEvent<Void>()
    val searchText = MutableLiveData("")

    fun search() {
        startSearchResultActivityEvent.call()
    }
}
