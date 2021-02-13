package me.nutyworks.syosetuviewerv2.ui.main.fragment.novel

import android.app.Application
import android.content.Intent
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.SyosetuViewerApplication
import me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.detail.NovelDetailAdapter
import me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.list.NovelListAdapter
import me.nutyworks.syosetuviewerv2.data.Novel
import me.nutyworks.syosetuviewerv2.data.NovelBody
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import me.nutyworks.syosetuviewerv2.utilities.ValidatorException
import org.jsoup.HttpStatusException

class NovelViewModel(application: Application) : AndroidViewModel(application) {

    companion object {
        private val TAG = NovelViewModel::class.simpleName
    }

    init {
        Log.i(TAG, "$TAG initialized!")
    }

    private val mRepository = NovelRepository.getInstance()

    private val mNovels = mRepository.novels
    private var mRecentlyDeletedNovel: Novel? = null
    private val mSelectedNovelBodies = mRepository.selectedNovelBodies
    val selectedNovel = ObservableField<Novel>()

    val novels: LiveData<List<Novel>> get() = mNovels
    val selectedNovelBodies: LiveData<List<NovelBody>> get() = mSelectedNovelBodies
    val novelListAdapter = NovelListAdapter(this)
    val novelDetailAdapter = NovelDetailAdapter(this)

    val listRecyclerViewIsVisible = ObservableBoolean(false)
    val notExistsIsVisible = ObservableBoolean(true)

    val detailRecyclerviewIsVisible = ObservableBoolean(false)
    val loadingProgressBarIsVisible = ObservableBoolean(false)

    val btnContinueIndex: Int
        get() {
            val novel = selectedNovel.get()!!

            return when (btnContinueType) {
                ContinueType.FIRST -> 1
                ContinueType.DEFAULT -> novel.recentWatchedEpisode
                ContinueType.NEXT -> novel.recentWatchedEpisode + 1
            }
        }

    val btnContinueText: Spannable
        get() {
            val app = getApplication<SyosetuViewerApplication>()
            val bodies = selectedNovelBodies.value!!

            val title = app.getString(
                when (btnContinueType) {
                    ContinueType.FIRST -> R.string.description_watch_first
                    ContinueType.DEFAULT -> R.string.description_continue_watch
                    ContinueType.NEXT -> R.string.description_watch_next
                }
            )
            val subtitle =
                bodies.find { it.index == btnContinueIndex }?.title?.text
                    ?: "Title unavailable"

            return SpannableString("$title\n$subtitle").apply {
                setSpan(
                    RelativeSizeSpan(.7f),
                    title.length,
                    title.length + subtitle.length + 1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
        }

    val btnContinueType: ContinueType
        get() {
            val novel = selectedNovel.get()!!
            val bodies = selectedNovelBodies.value!!

            return if (novel.recentWatchedEpisode == 0) {
                ContinueType.FIRST
            } else if (novel.recentWatchedPercent == 1f &&
                novel.recentWatchedEpisode != bodies.last { !it.isChapter }.index
            ) {
                ContinueType.NEXT
            } else {
                ContinueType.DEFAULT
            }
        }

    val dialogControlEvent = SingleLiveEvent<Void>()
    val snackBarNetworkFailEvent = SingleLiveEvent<Void>()
    val snackBarInvalidNcodeEvent = SingleLiveEvent<Void>()
    val novelDeleteEvent = SingleLiveEvent<Void>()

    val startNovelDetailFragmentEvent = SingleLiveEvent<Void>()
    val novelDetailFetchFinishEvent = SingleLiveEvent<Void>()
    val novelProgressChangeEvent = mRepository.novelProgressChangeEvent
    val scrollToTopEvent = SingleLiveEvent<Void>()

    val startNovelViewerActivityEvent = SingleLiveEvent<Float>()
    val selectedEpisode = ObservableField<NovelBody>()
    var previousReadIndexesSize = 0

    fun onNovelClick(novel: Novel) {
        Log.d(TAG, novel.toString())

        selectedNovel.set(novel)
        startNovelDetailFragmentEvent.call()

        GlobalScope.launch {
            mRepository.fetchSelectedNovelBodies(novel.ncode)
            withContext(Dispatchers.Main) {
                novelDetailFetchFinishEvent.call()
            }
        }
    }

    fun onNovelAddClick() {
        dialogControlEvent.call()
    }

    fun onEpisodeClick(position: Int, percent: Float = 0f) {
        Log.i(TAG, "episode clicked ${selectedNovelBodies.value?.get(position)}")
        selectedEpisode.set(selectedNovelBodies.value?.get(position))
        startNovelViewerActivityEvent.value = percent
        markAsRead(selectedNovel.get()!!, selectedEpisode.get()!!.index)
    }

    fun notifyListAdapterForUpdate() {
        Log.i(TAG, "notifyListAdapterForUpdate called, novels = ${novels.value}")
        novelListAdapter.notifyDataSetChanged()
    }

    fun notifyDetailAdapterForUpdate() {
        Log.i(TAG, "notifyDetailAdapterForUpdate called")
        novelDetailAdapter.notifyDataSetChanged()
    }

    private fun markAsRead(novel: Novel, index: Int) {
        GlobalScope.launch {
            mRepository.markAsRead(novel, index)
        }
    }

    fun isEpisodeMarkedAsRead(position: Int): Boolean {
        val index = selectedNovelBodies.value!![position].index

        return selectedNovel.get()!!.readIndexes.split(",").contains(index.toString())
    }

    fun insertNovel(ncode: String) {
        GlobalScope.launch {
            try {
                mRepository.insertNovel(ncode)
            } catch (e: ValidatorException) {
                Log.w(TAG, "Novel insert failed", e)
                withContext(Dispatchers.Main) {
                    snackBarInvalidNcodeEvent.call()
                }
            } catch (e: HttpStatusException) {
                Log.w(TAG, "Novel insert failed", e)
                withContext(Dispatchers.Main) {
                    snackBarInvalidNcodeEvent.call()
                }
            }
        }
    }

    fun deleteNovel(novel: Novel) {
        GlobalScope.launch {
            mRepository.deleteNovel(novel)
        }
    }

    fun deleteNovelIndex(position: Int) {
        val novel = mNovels.value?.get(position) ?: return
        mRecentlyDeletedNovel = novel
        deleteNovel(novel)
        novelDeleteEvent.call()
    }

    fun undoDelete() {
        GlobalScope.launch {
            mRecentlyDeletedNovel?.let {
                mRepository.insertNovel(it)
            }
        }
    }

    fun handleSendText(intent: Intent) {
        intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
            Log.i(TAG, "received from sharing, $it")

            """https://ncode.syosetu.com/([Nn]\d{4}[A-Za-z]{1,2})(?:/?.*)""".toRegex()
                .matchEntire(it)?.groups?.get(1)?.value?.let { ncode ->
                    insertNovel(ncode)
                } ?: snackBarInvalidNcodeEvent.call()
        }
    }

    fun onContinueButtonClick() {
        val novel = selectedNovel.get()!!
        val bodies = selectedNovelBodies.value!!
        val percent = if (btnContinueType == ContinueType.NEXT) 0f else novel.recentWatchedPercent
        onEpisodeClick(bodies.indexOfFirst { it.index == btnContinueIndex }, percent)
    }

    fun scrollToTop() {
        scrollToTopEvent.call()
    }
}

enum class ContinueType {
    FIRST, DEFAULT, NEXT
}
