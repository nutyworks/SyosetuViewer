package me.nutyworks.syosetuviewerv2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.nutyworks.syosetuviewerv2.databinding.FragmentNovelDetailBinding
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelViewModel

class NovelDetailFragment : Fragment() {

    companion object {
        private const val TAG = "NovelDetailFragment"
    }

    private val mViewModel: NovelViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNovelDetailBinding.inflate(layoutInflater, container, false)

        binding.viewModel = mViewModel

        setupListUiUpdate()

        return binding.root
    }

    private fun setupListUiUpdate() {
        with(mViewModel) {
            selectedNovelBodies.observe(viewLifecycleOwner) { notifyDetailAdapterForUpdate() }
            novelDetailFetchFinishEvent.observe(viewLifecycleOwner) {
                detailRecyclerviewIsVisible.set(true)
                loadingProgressBarIsVisible.set(false)
            }
        }
    }
}
