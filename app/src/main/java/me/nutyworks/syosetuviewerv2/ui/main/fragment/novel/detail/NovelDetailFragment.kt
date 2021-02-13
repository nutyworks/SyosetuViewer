package me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.nutyworks.syosetuviewerv2.databinding.FragmentNovelDetailBinding
import kotlin.properties.Delegates
import me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.NovelViewModel

class NovelDetailFragment : Fragment() {

    companion object {
        private const val TAG = "NovelDetailFragment"
    }

    private val mViewModel: NovelViewModel by activityViewModels()
    private var binding by Delegates.notNull<FragmentNovelDetailBinding>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNovelDetailBinding.inflate(layoutInflater, container, false)

        binding.viewModel = mViewModel

        setupListUiUpdate()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val selectedNovel = mViewModel.selectedNovel.get() ?: return
        val selectedNovelBodies = mViewModel.selectedNovelBodies.value ?: return
        selectedNovel.readIndexes.split(",").filter(String::isNotEmpty)
            .map(String::toInt).drop(mViewModel.previousReadIndexesSize).nullIfEmpty()?.let {
                val startIndex =
                    selectedNovelBodies.indexOfFirst { novel -> novel.index == it.first() }
                val endIndex =
                    selectedNovelBodies.indexOfFirst { novel -> novel.index == it.last() }

                mViewModel.novelDetailAdapter.notifyItemRangeChanged(
                    startIndex,
                    endIndex - startIndex + 1
                )
            }
    }

    private fun <S : Collection<T>, T> S.nullIfEmpty(): S? = if (this.none()) null else this

    private fun setupListUiUpdate() {
        with(mViewModel) {
            selectedNovelBodies.observe(viewLifecycleOwner) { notifyDetailAdapterForUpdate() }
            novelDetailFetchFinishEvent.observe(viewLifecycleOwner) {
                detailRecyclerviewIsVisible.set(true)
                loadingProgressBarIsVisible.set(false)
                updateContinueButtonText()
            }
            novelProgressChangeEvent.observe(viewLifecycleOwner) {
                updateContinueButtonText()
            }
            scrollToTopEvent.observe(viewLifecycleOwner) { this@NovelDetailFragment.scrollToTop() }
        }
    }

    private fun scrollToTop() {
        binding.svDetailWrapper.fullScroll(View.FOCUS_UP)
    }

    private fun updateContinueButtonText() {
        binding.btnContinue.text = mViewModel.btnContinueText
    }
}
