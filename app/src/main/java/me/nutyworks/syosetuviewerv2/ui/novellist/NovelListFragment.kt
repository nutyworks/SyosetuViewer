package me.nutyworks.syosetuviewerv2.ui.novellist

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.FragmentNovelListBinding
import me.nutyworks.syosetuviewerv2.ui.SwipeToDeleteCallback

class NovelListFragment : Fragment() {

    companion object {
        private val TAG = NovelListFragment::class.simpleName
    }

    private val mViewModel: NovelListViewModel by activityViewModels()
    private val mRoot: View by lazy { activity?.findViewById(android.R.id.content)!! }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentNovelListBinding.inflate(layoutInflater, container, false)
        mViewModel.novelListAdapter.context = requireContext()

        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(mViewModel.novelListAdapter))
        itemTouchHelper.attachToRecyclerView(binding.rvNovelList)

        binding.viewModel = mViewModel
        setupListUiUpdate()

        return binding.root
    }

    private fun setupListUiUpdate() {
        with(mViewModel) {
            novels.observe(viewLifecycleOwner) {
                notifyListAdapterForUpdate()
                notExistsIsVisible.set(it.isEmpty())
                recyclerViewIsVisible.set(it.isNotEmpty())
            }
            dialogControlEvent.observe(viewLifecycleOwner) { showDialogAddNovel() }
            snackBarNetworkFailEvent.observe(viewLifecycleOwner) { showNetworkFailSnackbar() }
            snackBarInvalidNcodeEvent.observe(viewLifecycleOwner) { showInvalidNcodeSnackbar() }
            novelDeleteEvent.observe(viewLifecycleOwner) { showUndoSnackbar() }
            startNovelDetailFragmentEvent.observe(viewLifecycleOwner) { startNovelDetailFragment() }
        }
    }

    private fun showNetworkFailSnackbar() {
        Log.i(TAG, "showNetworkFailSnackbar called")
        Snackbar.make(mRoot, "Failed to get novel, please try again.", Snackbar.LENGTH_LONG).show()
    }

    private fun showInvalidNcodeSnackbar() {
        Log.i(TAG, "showNetworkFailSnackbar called")
        Snackbar.make(mRoot, "Invalid ncode, please try again.", Snackbar.LENGTH_LONG).show()
    }

    private fun showDialogAddNovel() {
        Log.i(TAG, "showDialogAddNovel called")

        val linearLayout = layoutInflater.inflate(R.layout.layout_ncode_enter, null)
        val editText = linearLayout.findViewById<EditText>(R.id.et_ncode)

        AlertDialog.Builder(context)
            .setTitle(R.string.enter_ncode)
            .setMessage(R.string.enter_ncode_description)
            .setView(linearLayout)
            .setPositiveButton("Fetch") { dialog, _ ->
                mViewModel.insertNovel(editText.text.toString())
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun showUndoSnackbar() {
        Snackbar.make(mRoot, "Novel deleted.", Snackbar.LENGTH_LONG)
            .setAction("Undo") { undoDelete() }
            .show()
    }

    private fun startNovelDetailFragment() {
        view?.findNavController()?.navigate(
            NovelListFragmentDirections.actionFragmentNovelListToFragmentNovelDetail())
    }

    private fun undoDelete() {
        mViewModel.undoDelete()
    }
}