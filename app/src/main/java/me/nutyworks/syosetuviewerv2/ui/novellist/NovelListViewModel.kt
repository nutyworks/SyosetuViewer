package me.nutyworks.syosetuviewerv2.ui.novellist

import android.app.Application
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
import java.net.ProtocolException

class NovelListViewModel(application: Application) : AndroidViewModel(application) {

    init {
        Log.i(TAG, "$TAG initialized!")
    }

    companion object {
        private val TAG = NovelListViewModel::class.simpleName
    }

    private val mRepository = NovelEntityRepository(application).also {
        GlobalScope.launch {
            it.deleteAll()
        }
    }

    private val mNovels = mRepository.novels

    val novels: LiveData<List<NovelEntity>> get() = mNovels
    val adapter = NovelListAdapter(this)
    val recyclerViewIsVisible = ObservableBoolean(false)
    val notExistsIsVisible = ObservableBoolean(true)

    val dialogControlEvent = SingleLiveEvent<Void>()
    val snackBarNetworkFailEvent = SingleLiveEvent<Void>()

    fun onClick(novel: NovelEntity) {
        Log.d(TAG, novel.toString())
    }

    fun onNovelAddClick() {
        dialogControlEvent.call()

        GlobalScope.launch {
            try {
                insertNovel(listOf("n9433gn", "n1777ge", "n1992gr", "n7787eq").random())
            } catch (e: ProtocolException) {
                Log.w(TAG, "ProtocolException occurred")
                withContext(Dispatchers.Main) {
                    snackBarNetworkFailEvent.call()
                }
            }
        }
    }

    fun notifyAdapterForUpdate() {
        Log.i(TAG, "notifyAdapterForUpdate called, novels = ${novels.value}")
        adapter.notifyDataSetChanged()
    }

    private suspend fun insertNovel(ncode: String) = mRepository.insertNovel(ncode)
}