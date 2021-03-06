package me.nutyworks.syosetuviewerv2.ui.viewer

import android.content.Intent
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import me.nutyworks.syosetuviewerv2.data.NovelBody
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.data.TranslationWrapper
import me.nutyworks.syosetuviewerv2.data.wrap
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import java.io.IOException
import kotlin.properties.Delegates

class NovelViewerViewModel : ViewModel() {

    companion object {
        private const val TAG = "NovelViewerViewModel"
    }

    lateinit var ncode: String
    var index by Delegates.notNull<Int>()
    var lastIndex by Delegates.notNull<Int>()
    var percent by Delegates.notNull<Float>()

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
    val wordWrap = mRepository.wordWrap

    val startNextEpisodeViewerEvent = SingleLiveEvent<Void>()

    fun init(intent: Intent) {
        ncode = intent.getStringExtra(NovelViewerActivity.EXTRA_NCODE)!!
        index = intent.getIntExtra(NovelViewerActivity.EXTRA_INDEX, 0)
        lastIndex = intent.getIntExtra(NovelViewerActivity.EXTRA_LAST_INDEX, 0)
        percent = intent.getFloatExtra(NovelViewerActivity.EXTRA_PERCENT, 0f)

        fetchEpisode()
    }

    private fun fetchEpisode() {
        mViewModelScope.launch {
            try {
                mRepository.fetchEpisode(ncode, index)
            } catch (e: IOException) {
                Log.e(TAG, "networking error", e)
                novelBody.set(
                    NovelBody(
                        "-".wrap(),
                        false,
                        0,
                        listOf("Network error has occurred while fetching episode. Please try again.".wrap())
                    )
                )
            } catch (e: Exception) {
                novelBody.set(
                    NovelBody(
                        "-".wrap(),
                        false,
                        0,
                        listOf("Unknown error has occurred while fetching episode.".wrap())
                    )
                )
            } finally {
                mainTextUpdateEvent.call()
            }
        }
    }

    fun notifyAdapterForUpdate() {
        Log.i(TAG, "notifyAdapterForUpdate called")
        novelViewerAdapter.notifyDataSetChanged()
    }

    fun toggleTextLanguageType(position: Int) {
        (novelBody.get()?.mainTextWrappers?.get(position) as? TranslationWrapper)?.toggleViewType()
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

    fun changeWordWrap(wordWrap: Boolean) {
        mRepository.setWordWrap(wordWrap)
        novelViewerAdapter.invalidateAllBindingData()
    }

    fun setRecentWatched(percent: Float) {
        viewModelScope.launch {
            mRepository.setRecentWatched(ncode, index, percent)
        }
    }
}
