package me.nutyworks.syosetuviewerv2.ui.main.fragment.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import me.nutyworks.syosetuviewerv2.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    private val mViewModel: SettingsViewModel by activityViewModels()
    private lateinit var mBinding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentSettingsBinding.inflate(inflater, container, false)

        mBinding.viewModel = mViewModel

        mViewModel.event.observe(viewLifecycleOwner) {
            when (it) {
                is Event.SettingsAppliedEvent -> showSettingsAppliedSnackbar()
            }
        }

        return mBinding.root
    }

    private fun showSettingsAppliedSnackbar() {
        Snackbar.make(
            mBinding.root,
            "Settings applied. You may need restart for some options.",
            Snackbar.LENGTH_LONG
        ).show()
    }

    sealed class Event {
        object SettingsAppliedEvent : Event()
    }
}
