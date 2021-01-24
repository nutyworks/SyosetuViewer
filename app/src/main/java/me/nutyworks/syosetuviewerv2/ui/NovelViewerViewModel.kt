package me.nutyworks.syosetuviewerv2.ui

import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nutyworks.syosetuviewerv2.NovelViewerActivity
import me.nutyworks.syosetuviewerv2.adapter.NovelViewerAdapter
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import kotlin.properties.Delegates

class NovelViewerViewModel : ViewModel() {

    companion object {
        private const val TAG = "NovelViewerViewModel"
    }

    lateinit var ncode: String
    var index by Delegates.notNull<Int>()

    private val mViewModelScope = CoroutineScope(Job() + Dispatchers.Main)

    private val mRepository = NovelRepository.getInstance()

    val novelBody = mRepository.novelBody
    val novelViewerAdapter = NovelViewerAdapter(this)

    val mainTextUpdateEvent = SingleLiveEvent<Void>()

    val mainTextIsVisible = ObservableBoolean(false)
    val loadingProgressBarIsVisible = ObservableBoolean(true)
    val viewerSettingsIsVisible = ObservableBoolean(false)

    val textSize = mRepository.textSize
    val paragraphSpacing = mRepository.paragraphSpacing

    val startNextEpisodeViewerEvent = SingleLiveEvent<Void>()

    fun init(intent: Intent) {
        ncode = intent.getStringExtra(NovelViewerActivity.EXTRA_NCODE)!!
        index = intent.getIntExtra(NovelViewerActivity.EXTRA_INDEX, 0)

        fetchEpisode()
    }

    private fun fetchEpisode() {
        mViewModelScope.launch {
            mRepository.fetchEpisode(ncode, index)
            mainTextUpdateEvent.call()
        }
    }

    fun notifyAdapterForUpdate() {
        Log.i(TAG, "notifyAdapterForUpdate called")
        novelViewerAdapter.notifyDataSetChanged()
    }

    fun toggleTextLanguageType(position: Int) {
        novelBody.get()?.mainTextWrappers?.get(position)?.toggleViewType()
    }

    fun onNextEpisodeClick() {
        startNextEpisodeViewerEvent.call()
        mViewModelScope.launch {
            mRepository.markAsRead(ncode, index + 1)
        }
    }

    fun toggleViewerSettings() {
        viewerSettingsIsVisible.set(!viewerSettingsIsVisible.get())
    }

    fun changeTextSize(delta: Float) {
        mRepository.setTextSize(textSize.get() + delta)
    }

    fun changeParagraphSpacing(delta: Float) {
        mRepository.setParagraphSpacing(paragraphSpacing.get() + delta)
    }
}
