package me.nutyworks.syosetuviewerv2.ui

import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.NovelViewerActivity
import me.nutyworks.syosetuviewerv2.adapter.NovelViewerAdapter
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import kotlin.properties.Delegates

class NovelViewerViewModel : ViewModel() {

    companion object {
        private const val TAG = "NovelViewerViewModel"
    }

    lateinit var ncode: String
    var index by Delegates.notNull<Int>()

    private val mRepository = NovelRepository.getInstance()

    val novelMainText = mRepository.novelMainText
    val novelViewerAdapter = NovelViewerAdapter(this)

    val mainTextUpdateEvent = SingleLiveEvent<Void>()

    val mainTextIsVisible = ObservableBoolean(false)
    val loadingProgressBarIsVisible = ObservableBoolean(true)

    fun init(intent: Intent) {
        ncode = intent.getStringExtra(NovelViewerActivity.EXTRA_NCODE)!!
        index = intent.getIntExtra(NovelViewerActivity.EXTRA_INDEX, 0)

        fetchEpisode()
    }

    private fun fetchEpisode() {
        GlobalScope.launch {
            mRepository.fetchEpisode(ncode, index)
            withContext(Dispatchers.Main) {
                mainTextUpdateEvent.call()
            }
        }
    }

    fun notifyAdapterForUpdate() {
        Log.i(TAG, "notifyAdapterForUpdate called")
        novelViewerAdapter.notifyDataSetChanged()
    }

    fun toggleTextLanguageType(position: Int) {
        novelMainText.value?.let {
            it[position].viewType = when (it[position].viewType) {
                TranslationWrapper.ORIGINAL -> TranslationWrapper.TRANSLATED
                TranslationWrapper.TRANSLATED -> TranslationWrapper.ORIGINAL
                else -> TranslationWrapper.TRANSLATED
            }
        }
    }
}