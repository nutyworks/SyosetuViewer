package me.nutyworks.syosetuviewerv2.ui.novellist

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import me.nutyworks.syosetuviewerv2.adapter.NovelDetailAdapter
import me.nutyworks.syosetuviewerv2.adapter.NovelListAdapter
import me.nutyworks.syosetuviewerv2.data.Novel
import me.nutyworks.syosetuviewerv2.data.NovelBody
import me.nutyworks.syosetuviewerv2.data.NovelEntityRepository
import me.nutyworks.syosetuviewerv2.network.Narou
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
    private var mRecentlyDeletedNovel: Novel? = null
    val selectedNovel = ObservableField<Novel>()
    val selectedNovelBodies = ObservableField<List<NovelBody>>()

    val novels: LiveData<List<Novel>> get() = mNovels
    val novelListAdapter = NovelListAdapter(this)
    val novelDetailAdapter = NovelDetailAdapter(this)
    val recyclerViewIsVisible = ObservableBoolean(false)
    val notExistsIsVisible = ObservableBoolean(true)

    val dialogControlEvent = SingleLiveEvent<Void>()
    val snackBarNetworkFailEvent = SingleLiveEvent<Void>()
    val snackBarInvalidNcodeEvent = SingleLiveEvent<Void>()
    val novelDeleteEvent = SingleLiveEvent<Void>()
    val startNovelDetailFragmentEvent = SingleLiveEvent<Void>()

    fun onNovelClick(novel: Novel) {
        Log.d(TAG, novel.toString())

        selectedNovel.set(novel)
        startNovelDetailFragmentEvent.call()
    }

    fun onNovelAddClick() {
        dialogControlEvent.call()
    }

    fun notifyAdapterForUpdate() {
        Log.i(TAG, "notifyAdapterForUpdate called, novels = ${novels.value}")
        novelListAdapter.notifyDataSetChanged()
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
}