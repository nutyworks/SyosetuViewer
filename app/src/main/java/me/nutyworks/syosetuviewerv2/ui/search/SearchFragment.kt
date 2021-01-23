package me.nutyworks.syosetuviewerv2.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.nutyworks.syosetuviewerv2.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        private val TAG = SearchFragment::class.simpleName
    }

    private val mViewModel: SearchViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        binding.viewModel = mViewModel

        return binding.root
    }
}
