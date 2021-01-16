package me.nutyworks.syosetuviewerv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import me.nutyworks.syosetuviewerv2.databinding.ActivityNovelViewerBinding
import me.nutyworks.syosetuviewerv2.ui.NovelViewerViewModel

class NovelViewerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NCODE = "me.nutyworks.syosetuviewerv2.EXTRA_NCODE"
        const val EXTRA_INDEX = "me.nutyworks.syosetuviewerv2.EXTRA_INDEX"

        private const val TAG = "NovelViewerActivity"
    }

    private val mViewModel : NovelViewerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityNovelViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = mViewModel

        mViewModel.init(this.intent)

        setupUiUpdate()
    }

    private fun setupUiUpdate() {
        with(mViewModel) {
            mainTextUpdateEvent.observe(this@NovelViewerActivity) {
                mainTextIsVisible.set(true)
                loadingProgressBarIsVisible.set(false)
                notifyAdapterForUpdate()
            }
        }
    }
}