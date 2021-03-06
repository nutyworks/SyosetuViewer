package me.nutyworks.syosetuviewerv2.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import me.nutyworks.syosetuviewerv2.R
import me.nutyworks.syosetuviewerv2.databinding.ActivityMainBinding
import me.nutyworks.syosetuviewerv2.ui.main.fragment.novel.NovelViewModel
import me.nutyworks.syosetuviewerv2.ui.main.fragment.search.SearchViewModel
import me.nutyworks.syosetuviewerv2.ui.main.fragment.settings.SettingsViewModel
import me.nutyworks.syosetuviewerv2.ui.searchresult.SearchResultActivity
import me.nutyworks.syosetuviewerv2.ui.viewer.NovelViewerActivity

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mNovelViewModel: NovelViewModel by viewModels()
    private val mSearchViewModel: SearchViewModel by viewModels()
    private val mSettingsViewModel: SettingsViewModel by viewModels()

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        applySelectedTheme()
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
        binding.navView.setOnNavigationItemReselectedListener { }

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_novel_list,
                R.id.navigation_search,
                R.id.navigation_settings
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if (intent.type == "text/plain") {
                    mNovelViewModel.handleSendText(intent)
                }
            }
        }

        setupUiEvent()
    }

    private fun applySelectedTheme() {
        Log.i(TAG, "applySelectedTheme called")
        val theme = mSettingsViewModel.theme.get().let {
            when (it) {
                0 -> -1
                else -> it
            }
        }
        AppCompatDelegate.setDefaultNightMode(theme)
    }

    private fun setupUiEvent() {
        with(mNovelViewModel) {
            startNovelViewerActivityEvent.observe(this@MainActivity) { percent ->
                startNovelViewerActivity(percent)
            }
            snackbarText.observe(this@MainActivity, this@MainActivity::showSnackbar)
        }
        with(mSearchViewModel) {
            startSearchResultActivityEvent.observe(this@MainActivity) { startSearchResultActivity() }
        }
    }

    private fun showSnackbar(text: String) {
        Snackbar.make(binding.root, text, Snackbar.LENGTH_LONG).show()
    }

    private fun startNovelViewerActivity(percent: Float) {
        mNovelViewModel.previousReadIndexesSize =
            mNovelViewModel.selectedNovel.get()!!.readIndexes.count { it == ',' }

        startActivity(
            Intent(this, NovelViewerActivity::class.java).apply {
                Log.i(TAG, mNovelViewModel.selectedNovel.get()?.ncode.toString())
                Log.i(TAG, mNovelViewModel.selectedEpisode.get()?.index.toString())
                putExtra(
                    NovelViewerActivity.EXTRA_NCODE,
                    mNovelViewModel.selectedNovel.get()?.ncode
                )
                putExtra(
                    NovelViewerActivity.EXTRA_INDEX,
                    mNovelViewModel.selectedEpisode.get()?.index
                )
                putExtra(
                    NovelViewerActivity.EXTRA_LAST_INDEX,
                    mNovelViewModel.selectedNovelBodies.value?.last { !it.isChapter }?.index
                )
                putExtra(
                    NovelViewerActivity.EXTRA_PERCENT,
                    percent
                )
            }
        )
    }

    private fun startSearchResultActivity() {
        Log.i(TAG, "startSearchResultActivity called ${mSearchViewModel.includeWords.value}")
        startActivity(
            Intent(this, SearchResultActivity::class.java).apply {
                putExtra(
                    SearchResultActivity.INTENT_SEARCH_REQUIREMENTS,
                    mSearchViewModel.getRequirements()
                )
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "Back button pressed")
        super.onBackPressed()

        return super.onOptionsItemSelected(item)
    }
}
