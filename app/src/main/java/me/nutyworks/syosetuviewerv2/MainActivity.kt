package me.nutyworks.syosetuviewerv2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.nutyworks.syosetuviewerv2.ui.novellist.NovelViewModel
import me.nutyworks.syosetuviewerv2.ui.search.SearchViewModel

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private val mNovelViewModel: NovelViewModel by viewModels()
    private val mSearchViewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
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
                R.id.navigation_notifications
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

        with(mNovelViewModel) {
            startNovelViewerActivityEvent.observe(this@MainActivity) { startNovelViewerActivity() }
        }
    }

    override fun onResume() {
        super.onResume()

        // TODO heavy task; need to be optimized
        mNovelViewModel.novelDetailAdapter.notifyDataSetChanged()
    }

    private fun startNovelViewerActivity() {
        startActivity(
            Intent(this, NovelViewerActivity::class.java).apply {
                Log.i(TAG, mNovelViewModel.selectedNovel.get()?.ncode.toString())
                Log.i(TAG, mNovelViewModel.selectedEpisode.get()?.index.toString())
                putExtra(NovelViewerActivity.EXTRA_NCODE, mNovelViewModel.selectedNovel.get()?.ncode)
                putExtra(NovelViewerActivity.EXTRA_INDEX, mNovelViewModel.selectedEpisode.get()?.index)
            }
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.i(TAG, "Back button pressed")
        super.onBackPressed()

        return super.onOptionsItemSelected(item)
    }
}
