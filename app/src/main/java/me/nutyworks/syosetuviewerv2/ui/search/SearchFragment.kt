package me.nutyworks.syosetuviewerv2.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        private val TAG = SearchFragment::class.simpleName
    }

    private val mViewModel: SearchViewModel by activityViewModels()
    private lateinit var binding: FragmentSearchBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)

        binding.viewModel = mViewModel
        binding.spinnerOrderBy.adapter =
            ArrayAdapter.createFromResource(
                requireContext(),
                R.array.order_by_array,
                android.R.layout.simple_spinner_item
            ).apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            }

        mViewModel.genreExpansionToggleEvent.observe(viewLifecycleOwner) { toggleGenreExpansion() }

        return binding.root
    }

    private fun toggleGenreExpansion() {
        binding.elFoldGenre.toggle()
    }
}
