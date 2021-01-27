package me.nutyworks.syosetuviewerv2.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import me.nutyworks.syosetuviewerv2.network.Yomou
import me.nutyworks.syosetuviewerv2.utilities.SingleLiveEvent

class SearchViewModel : ViewModel() {

    val startSearchResultActivityEvent = SingleLiveEvent<Void>()
    val genreExpansionToggleEvent = SingleLiveEvent<Void>()
    val searchText = MutableLiveData("")
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

    val genre: IntArray
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
        }.toIntArray()

    fun toggleGenreExpansion() {
        genreExpansionToggleEvent.call()
    }

    fun search() {
        startSearchResultActivityEvent.call()
    }
}
