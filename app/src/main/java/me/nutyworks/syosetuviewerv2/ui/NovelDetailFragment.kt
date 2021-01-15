package me.nutyworks.syosetuviewerv2.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.FragmentNovelDetailBinding
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelListViewModel

class NovelDetailFragment : Fragment() {

    private val mViewModel: NovelListViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNovelDetailBinding.inflate(layoutInflater, container, false)

        binding.viewModel = mViewModel

        return binding.root
    }
}