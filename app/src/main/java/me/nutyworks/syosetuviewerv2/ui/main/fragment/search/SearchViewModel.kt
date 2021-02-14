package me.nutyworks.syosetuviewerv2.ui.main.fragment.search

import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.nutyworks.syosetuviewerv2.data.SearchRequirements
import me.nutyworks.syosetuviewerv2.network.Yomou
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent
import java.lang.IllegalArgumentException

class SearchViewModel : ViewModel() {

    companion object {
        private const val TAG = "SearchViewModel"
    }

    val startSearchResultActivityEvent = SingleLiveEvent<Void>()
    val genreExpansionToggleEvent = SingleLiveEvent<Void>()
    val advancedExpansionToggleEvent = SingleLiveEvent<Void>()
    val includeWords = MutableLiveData("")
    val excludeWords = MutableLiveData("")
    val orderBy = MutableLiveData(0)

    // Genres
    val genreDifferentWorld = MutableLiveData(false)
    val genreRealWorld = MutableLiveData(false)
    val genreHighFantasy = MutableLiveData(false)
    val genreLowFantasy = MutableLiveData(false)
    val genrePureLiterature = MutableLiveData(false)
    val genreHumanDrama = MutableLiveData(false)
    val genreHistory = MutableLiveData(false)
    val genreDetective = MutableLiveData(false)
    val genreHorror = MutableLiveData(false)
    val genreAction = MutableLiveData(false)
    val genreComedy = MutableLiveData(false)
    val genreVrGame = MutableLiveData(false)
    val genreUniverse = MutableLiveData(false)
    val genreSciFi = MutableLiveData(false)
    val genrePanic = MutableLiveData(false)
    val genreFairyTale = MutableLiveData(false)
    val genrePoetry = MutableLiveData(false)
    val genreEssay = MutableLiveData(false)
    val genreReplay = MutableLiveData(false)
    val genreOther = MutableLiveData(false)
    val genreNonGenre = MutableLiveData(false)

    private val genres: List<Int>
        get() = mutableListOf<Int>().apply {
            if (genreDifferentWorld.value == true) add(Yomou.Genre.DIFFERENT_WORLD)
            if (genreRealWorld.value == true) add(Yomou.Genre.REAL_WORLD)
            if (genreHighFantasy.value == true) add(Yomou.Genre.HIGH_FANTASY)
            if (genreLowFantasy.value == true) add(Yomou.Genre.LOW_FANTASY)
            if (genrePureLiterature.value == true) add(Yomou.Genre.PURE_LITERATURE)
            if (genreHumanDrama.value == true) add(Yomou.Genre.HUMAN_DRAMA)
            if (genreHistory.value == true) add(Yomou.Genre.HISTORY)
            if (genreDetective.value == true) add(Yomou.Genre.DETECTIVE)
            if (genreHorror.value == true) add(Yomou.Genre.HORROR)
            if (genreAction.value == true) add(Yomou.Genre.ACTION)
            if (genreComedy.value == true) add(Yomou.Genre.COMEDY)
            if (genreVrGame.value == true) add(Yomou.Genre.VR_GAME)
            if (genreUniverse.value == true) add(Yomou.Genre.UNIVERSE)
            if (genreSciFi.value == true) add(Yomou.Genre.FANTASY_SCIENCE)
            if (genrePanic.value == true) add(Yomou.Genre.PANIC)
            if (genreFairyTale.value == true) add(Yomou.Genre.FAIRY_TALE)
            if (genrePoetry.value == true) add(Yomou.Genre.POETRY)
            if (genreEssay.value == true) add(Yomou.Genre.ESSAY)
            if (genreReplay.value == true) add(Yomou.Genre.REPLAY)
            if (genreOther.value == true) add(Yomou.Genre.OTHER)
            if (genreNonGenre.value == true) add(Yomou.Genre.NON_GENRE)
        }

    // Advanced
    val requireShort = MutableLiveData(false)
    val requireInSerialization = MutableLiveData(false)
    val requireFinished = MutableLiveData(false)
    private val requireType: String
        get() = {
            val t = if (requireShort.value == true) 1 else 0
            val r = if (requireInSerialization.value == true) 1 else 0
            val er = if (requireFinished.value == true) 1 else 0

            when (t * 4 + r * 2 + er) {
                0b001 -> "er"
                0b010 -> "r"
                0b011 -> "re"
                0b100 -> "t"
                0b101 -> "ter"
                0b110 -> "tr"
                0b111 -> "all"
                else -> ""
            }
        }()
    val minTime = MutableLiveData("")
    val maxTime = MutableLiveData("")
    val minLen = MutableLiveData("")
    val maxLen = MutableLiveData("")
    val minGlobalPoint = MutableLiveData("")
    val maxGlobalPoint = MutableLiveData("")
    val minLastUp = ObservableField("")
    val maxLastUp = ObservableField("")
    val minFirstUp = ObservableField("")
    val maxFirstUp = ObservableField("")

    fun toggleGenreExpansion() {
        genreExpansionToggleEvent.call()
    }

    fun toggleAdvancedExpansion() {
        advancedExpansionToggleEvent.call()
    }

    fun setDateFinished(type: String, result: String) {
        Log.i(TAG, "set date fin $type $result")
        when (type) {
            "nl" -> minLastUp
            "xl" -> maxLastUp
            "nf" -> minFirstUp
            "xf" -> maxFirstUp
            else -> throw IllegalArgumentException("Unknown type $type")
        }.set(result)
    }

    fun getRequirements(): SearchRequirements =
        SearchRequirements(
            includeWords.value ?: "",
            excludeWords.value ?: "",
            genres,
            requireType,
            minTime.value?.toIntOrNull(),
            maxTime.value?.toIntOrNull(),
            minLen.value?.toIntOrNull(),
            maxLen.value?.toIntOrNull(),
            minGlobalPoint.value?.toIntOrNull(),
            maxGlobalPoint.value?.toIntOrNull(),
            minLastUp.get() ?: "",
            maxLastUp.get() ?: "",
            minFirstUp.get() ?: "",
            maxFirstUp.get() ?: "",
            orderBy.value ?: 0,
        )

    fun search() {
        startSearchResultActivityEvent.call()
    }
}
