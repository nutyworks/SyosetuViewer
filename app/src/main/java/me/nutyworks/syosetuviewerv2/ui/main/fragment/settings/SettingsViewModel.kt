package me.nutyworks.syosetuviewerv2.ui.main.fragment.settings

import android.util.Log
import androidx.databinding.ObservableInt
import androidx.lifecycle.ViewModel
import me.nutyworks.syosetuviewerv2.data.NovelRepository
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent

class SettingsViewModel : ViewModel() {

    companion object {
        const val TAG = "SettingsViewModel"
    }

    private val mRepository = NovelRepository.getInstance()
    val theme = ObservableInt(mRepository.theme)
    val isOverR18 = mRepository.isOver18
    val event = SingleLiveEvent<SettingsFragment.Event>()

    fun applySettings() {
        Log.i(TAG, "applySettings ${theme.get()}")
        mRepository.theme = theme.get()
        mRepository.setOver18(isOverR18.get())
        event.value = SettingsFragment.Event.SettingsAppliedEvent
    }
}
