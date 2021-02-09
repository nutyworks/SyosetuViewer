package me.nutyworks.syosetuviewerv2

import android.app.Application
import me.nutyworks.syosetuviewerv2.data.NovelRepository

class SyosetuViewerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        NovelRepository.getInstance(this)
    }
}
