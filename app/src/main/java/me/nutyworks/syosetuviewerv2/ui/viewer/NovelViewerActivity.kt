package me.nutyworks.syosetuviewerv2.ui.viewer

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import me.nutyworks.syosetuviewerv2.databinding.ActivityNovelViewerBinding
import kotlin.properties.Delegates

class NovelViewerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_NCODE = "me.nutyworks.syosetuviewerv2.EXTRA_NCODE"
        const val EXTRA_INDEX = "me.nutyworks.syosetuviewerv2.EXTRA_INDEX"
        const val EXTRA_LAST_INDEX = "me.nutyworks.syosetuviewerv2.EXTRA_LAST_INDEX"
        const val EXTRA_PERCENT = "me.nutyworks.syosetuviewerv2.EXTRA_PERCENT"

        private const val TAG = "NovelViewerActivity"
    }

    private val mViewModel: NovelViewerViewModel by viewModels()
    private var binding by Delegates.notNull<ActivityNovelViewerBinding>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNovelViewerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = mViewModel

        mViewModel.init(this.intent)

        setupUiUpdate()
    }

    override fun onPause() {
        super.onPause()

        val sv = binding.svContentWrapper
        (sv.scrollY.toFloat() / (sv.getChildAt(0).bottom - sv.height)).let {
            if (it.isNaN()) return@let
            mViewModel.setRecentWatched(it)
        }
    }

    private fun setupUiUpdate() {
        with(mViewModel) {
            mainTextUpdateEvent.observe(this@NovelViewerActivity) {
                mainTextIsVisible.set(true)
                loadingProgressBarIsVisible.set(false)
                notifyAdapterForUpdate()
                Handler(Looper.getMainLooper()).postDelayed(50) {
                    setScroll(mViewModel.percent)
                }
            }
            startNextEpisodeViewerEvent.observe(this@NovelViewerActivity) {
                startNextEpisodeViewer()
            }
        }
    }

    private fun setScroll(percent: Float) {
        val sv = binding.svContentWrapper

        sv.scrollTo(0, (percent * (sv.getChildAt(0).bottom - sv.height)).toInt())
    }

    private fun startNextEpisodeViewer() {
        startActivity(
            Intent(this, NovelViewerActivity::class.java).apply {
                putExtra(EXTRA_NCODE, mViewModel.ncode)
                putExtra(EXTRA_INDEX, mViewModel.index + 1)
                putExtra(EXTRA_LAST_INDEX, mViewModel.lastIndex)
                putExtra(EXTRA_PERCENT, 0f)
            }
        )
        finish()
    }
}
