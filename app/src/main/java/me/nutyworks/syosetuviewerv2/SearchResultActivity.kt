package me.nutyworks.syosetuviewerv2

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import me.nutyworks.syosetuviewerv2.databinding.ActivitySearchResultBinding
import me.nutyworks.syosetuviewerv2.ui.SearchResultViewModel

class SearchResultActivity : AppCompatActivity() {

    companion object {
        private val TAG = SearchResultActivity::class.simpleName
        const val INTENT_INCLUDE_WORDS = "me.nutyworks.syosetuviewerv2.INTENT_INCLUDE_WORDS"
    }

    private val mViewModel: SearchResultViewModel by viewModels()
    private lateinit var mRoot: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivitySearchResultBinding.inflate(layoutInflater)

        supportActionBar?.title = getString(R.string.title_search_results)

        binding.viewModel = mViewModel
        mViewModel.init(intent)

        mRoot = binding.root

        setContentView(binding.root)

        setupUiEvent()
    }

    private fun setupUiEvent() {
        with(mViewModel) {
            searchResultsUpdateEvent.observe(this@SearchResultActivity) {
                notifyListAdapterForUpdate()
                resultsRecyclerViewIsVisible.set(true)
                loadingProgressBarIsVisible.set(false)
            }
            addNovelEvent.observe(this@SearchResultActivity) { showAddNovelSnackbar() }
        }
    }

    private fun showAddNovelSnackbar() {
        Snackbar.make(mRoot, "Adding novels to your library.", Snackbar.LENGTH_LONG).show()
    }
}
