package me.nutyworks.syosetuviewerv2.ui.main.fragment.search

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        private const val TAG = "SearchFragment"
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
        mViewModel.advancedExpansionToggleEvent.observe(viewLifecycleOwner) { toggleAdvancedExpansion() }

        setDatePickerListener(binding.dpMinLastUp, "nl")
        setDatePickerListener(binding.dpMaxLastUp, "xl")
        setDatePickerListener(binding.dpMinFirstUp, "nf")
        setDatePickerListener(binding.dpMaxFirstUp, "xf")

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setDatePickerListener(view: View, type: String) {
        view.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showDatePicker(type)
            }
            true
        }
    }

    private fun showDatePicker(type: String) {
        Log.i(TAG, "showDatePicker $type")
        DatePickerDialog(requireContext()).apply {
            setOnDateSetListener { _, year, month, dayOfMonth ->
                mViewModel.setDateFinished(type, "%4d/%02d/%02d".format(year, month + 1, dayOfMonth))
            }
            setOnCancelListener {
                mViewModel.setDateFinished(type, "")
            }
        }.show()
    }

    private fun toggleGenreExpansion() {
        binding.elFoldGenre.toggle()
    }

    private fun toggleAdvancedExpansion() {
        binding.elFoldAdvanced.toggle()
    }
}
