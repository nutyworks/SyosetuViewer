package me.nutyworks.syosetuviewerv2.ui.novellist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import me.nutyworks.syosetuviewerv2.databinding.FragmentNovelListBinding

class NovelListFragment : Fragment() {

    companion object {
        private val TAG = NovelListFragment::class.simpleName
    }

    private val mViewModel by lazy { ViewModelProvider(this).get(NovelListViewModel::class.java) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNovelListBinding.inflate(layoutInflater, container, false)

        binding.fragmentNovelListViewModel = mViewModel
        setupListUiUpdate()

        return binding.root
    }

    private fun setupListUiUpdate() {
        Log.i(TAG, "subscribeUi called")
        mViewModel.novels.observe(viewLifecycleOwner) { novels ->
            Log.i(TAG, "observer executed")
            Log.i(TAG, novels.isEmpty().toString())
            if (novels.isEmpty()) {
                mViewModel.notExistsVisibility.set(View.VISIBLE)
                mViewModel.novelListVisibility.set(View.GONE)
            } else {
                mViewModel.notExistsVisibility.set(View.GONE)
                mViewModel.novelListVisibility.set(View.VISIBLE)
                mViewModel.notifyAdapterForUpdate()
            }
        }
    }
}