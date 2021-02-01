package me.nutyworks.syosetuviewerv2

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import me.nutyworks.syosetuviewerv2.databinding.ActivityNovelViewerBinding
import me.nutyworks.syosetuviewerv2.ui.NovelViewerViewModel

class NovelViewerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NCODE = "me.nutyworks.syosetuviewerv2.EXTRA_NCODE"
        const val EXTRA_INDEX = "me.nutyworks.syosetuviewerv2.EXTRA_INDEX"
        const val EXTRA_LAST_INDEX = "me.nutyworks.syosetuviewerv2.EXTRA_LAST_INDEX"

        private const val TAG = "NovelViewerActivity"
    }

    private val mViewModel: NovelViewerViewModel by viewModels()

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
            startNextEpisodeViewerEvent.observe(this@NovelViewerActivity) {
                startNextEpisodeViewer()
            }
        }
    }

    private fun startNextEpisodeViewer() {
        startActivity(
            Intent(this, NovelViewerActivity::class.java).apply {
                putExtra(EXTRA_NCODE, mViewModel.ncode)
                putExtra(EXTRA_INDEX, mViewModel.index + 1)
                putExtra(EXTRA_LAST_INDEX, mViewModel.lastIndex)
            }
        )
        finish()
    }
}
