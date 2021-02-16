package me.nutyworks.syosetuviewerv2

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import me.nutyworks.syosetuviewerv2.data.MIGRATION_3_4
import me.nutyworks.syosetuviewerv2.data.NovelDatabase
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NovelMigrationTest {

    private val testDb = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        NovelDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    fun migrate3To4() {
        helper.createDatabase(testDb, 3).apply {
            execSQL(
                """
                CREATE TABLE IF NOT EXISTS novels(
                    ncode TEXT PRIMARY KEY NOT NULL,
                    title TEXT NOT NULL DEFAULT '',
                    translatedTitle TEXT NOT NULL DEFAULT '',
                    writer TEXT NOT NULL DEFAULT '',
                    readIndexes TEXT NOT NULL DEFAULT '',
                    recentWatchedEpisode INT NOT NULL DEFAULT 0,
                    recentWatchedPercent FLOAT NOT NULL DEFAULT 0
                )
                """.trimIndent()
            )
            execSQL("""INSERT INTO novels values ('n4154fl', 'test title1', 'test translated title1', 'test writer1', '', 1, 98.5)""")
            execSQL("""INSERT INTO novels values ('n1234b', 'test title2', 'test translated title2', 'test writer2', '', 54, 1)""")

            close()
        }

        helper.runMigrationsAndValidate(testDb, 4, true, MIGRATION_3_4)
    }
}
