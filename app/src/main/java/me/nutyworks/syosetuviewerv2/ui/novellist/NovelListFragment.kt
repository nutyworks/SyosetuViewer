package me.nutyworks.syosetuviewerv2.ui.novellist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import me.nutyworks.syosetuviewerv2.databinding.FragmentNovelListBinding

class NovelListFragment : Fragment() {

    companion object {
        private val TAG = NovelListFragment::class.simpleName
    }

    private val mViewModel by lazy { ViewModelProvider(this).get(NovelListViewModel::class.java) }
    private val mRoot: View by lazy { activity?.findViewById(android.R.id.content)!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNovelListBinding.inflate(layoutInflater, container, false)

        binding.viewModel = mViewModel
        setupListUiUpdate()

        return binding.root
    }

    private fun setupListUiUpdate() {
        mViewModel.novels.observe(viewLifecycleOwner) {
            mViewModel.notifyAdapterForUpdate()
            mViewModel.notExistsIsVisible.set(it.isEmpty())
            mViewModel.recyclerViewIsVisible.set(it.isNotEmpty())
        }
        mViewModel.dialogControlEvent.observe(viewLifecycleOwner) { showDialogAddNovel() }
        mViewModel.snackBarNetworkFailEvent.observe(viewLifecycleOwner) { showNetworkFailSnackbar() }
    }

    private fun showNetworkFailSnackbar() {
        Log.i(TAG, "showNetworkFailSnackbar called")
        Snackbar.make(mRoot, "Failed to get novel, please try again.", Snackbar.LENGTH_LONG).show()
    }

    private fun showDialogAddNovel() {
        Log.i(TAG, "showDialogAddNovel called")
    }
}