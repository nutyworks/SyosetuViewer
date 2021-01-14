package me.nutyworks.syosetuviewerv2.ui.novellist

import android.util.Log
import android.view.View
import androidx.databinding.ObservableInt
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.nutyworks.syosetuviewerv2.adapter.NovelListAdapter
import me.nutyworks.syosetuviewerv2.data.ChapteredNovel
import me.nutyworks.syosetuviewerv2.data.INovel

class NovelListViewModel : ViewModel() {

    init {
        Log.i(TAG, "$TAG initialized!")
    }

    companion object {
        private val TAG = NovelListViewModel::class.simpleName
    }
    private val mNovels = MutableLiveData<List<INovel>>().apply { value = listOf() }

    val novels: LiveData<List<INovel>> get() = mNovels
    val adapter = NovelListAdapter(this)
    val notExistsVisibility = ObservableInt(View.VISIBLE)
    val novelListVisibility = ObservableInt(View.GONE)

    fun onClick(novel: INovel) {
        Log.d(TAG, novel.toString())
    }

    fun onNovelAddClick() {
        mNovels.postValue(
            mNovels.value?.plus(ChapteredNovel(
                "Novel ${(0..10000).random()}",
                "Writer ${(0..10000).random()}",
                "something",
                listOf()
            ))
        )
    }

    fun notifyAdapterForUpdate() {
        adapter.notifyDataSetChanged()
    }
}