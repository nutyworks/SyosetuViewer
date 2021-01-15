package me.nutyworks.syosetuviewerv2.ui.novellist

import android.app.Application
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableInt
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.adapter.NovelListAdapter
import me.nutyworks.syosetuviewerv2.data.NovelEntity
import me.nutyworks.syosetuviewerv2.data.NovelEntityRepository
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import java.lang.IllegalArgumentException
import java.net.ProtocolException

class NovelListViewModel(application: Application) : AndroidViewModel(application) {

    init {
        Log.i(TAG, "$TAG initialized!")
    }

    companion object {
        private val TAG = NovelListViewModel::class.simpleName
    }

    private val mRepository = NovelEntityRepository(application)

    private val mNovels = mRepository.novels
    private var mRecentlyDeletedNovel: NovelEntity? = null
    private var selectedNovel: NovelEntity? = null

    val novels: LiveData<List<NovelEntity>> get() = mNovels
    val adapter = NovelListAdapter(this)
    val recyclerViewIsVisible = ObservableBoolean(false)
    val notExistsIsVisible = ObservableBoolean(true)

    val dialogControlEvent = SingleLiveEvent<Void>()
    val snackBarNetworkFailEvent = SingleLiveEvent<Void>()
    val snackBarInvalidNcodeEvent = SingleLiveEvent<Void>()
    val novelDeleteEvent = SingleLiveEvent<Void>()
    val startNovelDetailEvent = SingleLiveEvent<Void>()

    fun onNovelClick(novel: NovelEntity) {
        Log.d(TAG, novel.toString())

        startNovelDetailEvent.call()
        selectedNovel = novel
    }

    fun onNovelAddClick() {
        dialogControlEvent.call()
    }

    fun notifyAdapterForUpdate() {
        Log.i(TAG, "notifyAdapterForUpdate called, novels = ${novels.value}")
        adapter.notifyDataSetChanged()
    }

    fun insertNovel(ncode: String) {
        GlobalScope.launch {
            try {
                mRepository.insertNovel(ncode)
            } catch (e: ProtocolException) {
                Log.w(TAG, "ProtocolException occurred while fetching syosetu.com")
                withContext(Dispatchers.Main) {
                    snackBarNetworkFailEvent.call()
                }
            } catch (e: IllegalArgumentException) {
                Log.w(TAG, "Invalid ncode passed")
                withContext(Dispatchers.Main) {
                    snackBarInvalidNcodeEvent.call()
                }
            }
        }
    }

    fun deleteNovel(novel: NovelEntity) {
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
}