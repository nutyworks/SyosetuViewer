package me.nutyworks.syosetuviewerv2

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
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelViewModel
import me.nutyworks.syosetuviewerv2.ui.search.SearchViewModel
import me.nutyworks.syosetuviewerv2.ui.settings.SettingsViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mNovelViewModel: NovelViewModel by viewModels()
    private val mSearchViewModel: SearchViewModel by viewModels()
    private val mSettingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        applySelectedTheme()
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemReselectedListener { }

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
        navView.setupWithNavController(navController)

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
            startNovelViewerActivityEvent.observe(this@MainActivity) { startNovelViewerActivity() }
        }
        with(mSearchViewModel) {
            startSearchResultActivityEvent.observe(this@MainActivity) { startSearchResultActivity() }
        }
    }

    private fun startNovelViewerActivity() {
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
