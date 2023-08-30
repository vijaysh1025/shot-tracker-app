package com.shottracker.feature.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shottracker.feature.home.data.PlayByPlayRepository
import com.shottracker.feature.home.data.PlayByPlayRepositoryImpl
import com.shottracker.feature.home.models.pbp.Away
import com.shottracker.feature.home.models.pbp.EventType
import com.shottracker.feature.home.models.pbp.EventsItem
import com.shottracker.feature.home.models.pbp.Home
import com.shottracker.feature.home.models.pbp.Location
import com.shottracker.feature.home.models.pbp.PlayByPlay
import com.shottracker.feature.home.models.pbp.Player
import com.shottracker.feature.home.ui.models.PlayType
import com.shottracker.feature.home.ui.models.PlayUi
import com.shottracker.feature.home.ui.models.PlayerUi
import com.shottracker.feature.home.ui.models.StatType
import com.shottracker.feature.home.ui.models.TeamUi
import com.shottracker.feature.home.ui.states.PbpUiState
import com.shottracker.network.Result
import com.shottracker.utils.LiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class PlayByPlayViewModel @Inject constructor(
    private val playByPlayRepository: PlayByPlayRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    /**
     * Manage screen state
     */
    private val _pbpUiState: LiveEvent<PbpUiState> = LiveEvent()
    val pbpUiState: LiveData<PbpUiState> = _pbpUiState

    /**
     * Manage screen state
     */
    private val _selectedTeam: LiveEvent<TeamUi> = LiveEvent()
    val selectedTeam: LiveData<TeamUi> = _selectedTeam

    /**
     * Manage screen state
     */
    private val _selectedPlayer: LiveEvent<PlayerUi> = LiveEvent()
    val selectedPlayer: LiveData<PlayerUi> = _selectedPlayer


    private val _showCourt:LiveEvent<Boolean> = LiveEvent()
    val showCourt:LiveData<Boolean> = _showCourt

    fun selectTeam(teamUi: TeamUi) {
        _selectedTeam.value = teamUi
    }

    fun selectPlayer(playerUi: PlayerUi) {
        _selectedPlayer.value = playerUi
    }

    init {
        _showCourt.value = false
    }

    private suspend fun processData(playByPlay: PlayByPlay) = withContext(ioDispatcher) {
        val homeTeamUi = playByPlay.home?.toTeamUi()
        val awayTeamUi = playByPlay.away?.toTeamUi()

        playByPlay.periods?.forEach { period ->
            period?.events?.forEach { event ->
                val isHome = event?.attribution?.id == homeTeamUi?.id
                event?.let {
                    when {
                        isHome -> homeTeamUi?.updatePlayer(it)
                        else -> awayTeamUi?.updatePlayer(it)
                    }
                }
            }
        }

        if(homeTeamUi != null && awayTeamUi != null) {
            _pbpUiState.postValue(PbpUiState.Success(homeTeamUi, awayTeamUi))
            homeTeamUi.let {
                _selectedTeam.postValue(it)
//                it.players.values.first().let { player ->
//                    _selectedPlayer.postValue(player)
//                }
            }
        }
        homeTeamUi?.players?.forEach{
            val player = it.value
            player.stats.forEach { stat->
                Log.d("STATS", "${player.name} | ${stat.value.type} : ${stat.value.count}")
            }
            Log.d("STATS", "-----------------------")

        }

        delay(2000)
        _showCourt.postValue(true)
    }


    fun fetchPlayByPlay(gameId: String) {
        dispatchPbpUiState(PbpUiState.Loading)
        viewModelScope.launch {
            when (val result = playByPlayRepository.getPlayByPlay(gameId)) {
                is Result.Failure -> dispatchPbpUiState(PbpUiState.ErrorRetry)
                is Result.Success -> result.data?.let {
                    processData(it)
                }
                else -> {}
            }
        }
    }


    private fun dispatchPbpUiState(state: PbpUiState) {
        _pbpUiState.postValue(state)
    }

    private fun Home.toTeamUi(): TeamUi {
        return TeamUi(
            id = id ?: "",
            name = name ?: "",
            alias = alias ?: ""
        )
    }

    private fun Away.toTeamUi(): TeamUi {
        return TeamUi(
            id = id ?: "",
            name = name ?: "",
            alias = alias ?: ""
        )
    }

    private fun TeamUi.updatePlayer(event: EventsItem) {
        val playerList = event.statistics?.map { Pair(it?.type, it?.player) }

        playerList?.find { it.first == "fieldgoal" }?.second?.apply {
            if (!players.containsKey(jerseyNumber) && jerseyNumber != null) {
                players.put(
                    jerseyNumber, PlayerUi(
                        name = fullName ?: "",
                        number = jerseyNumber,
                    )
                )
            }

            jerseyNumber?.let {
                val locPercent = event.location?.toPercent() ?: Pair(0f, 0f)
                when (event.eventType) {
                    EventType.twopointmade.toString() -> players[it]?.apply {
                        plays.add(
                            PlayUi(
                                PlayType.SHOT_MADE,
                                locPercent.first,
                                locPercent.second
                            )
                        )
                        stats[StatType.POINTS]?.apply {
                            count += 2
                        }
                    }

                    EventType.twopointmiss.toString()  -> players[it]?.apply {
                        plays.add(
                            PlayUi(
                                PlayType.SHOT_MISSED,
                                locPercent.first,
                                locPercent.second
                            )
                        )
                    }

                    EventType.threepointmade.toString()  -> players[it]?.apply {
                        plays.add(
                            PlayUi(
                                PlayType.SHOT_MADE,
                                locPercent.first,
                                locPercent.second
                            )
                        )
                        stats[StatType.POINTS]?.apply {
                            count += 3
                        }
                    }

                    EventType.threepointmiss.toString()  -> players[it]?.apply {
                        plays.add(
                            PlayUi(
                                PlayType.SHOT_MISSED,
                                locPercent.first,
                                locPercent.second
                            )
                        )
                    }
                    else -> {}
                }
            }
        }

        playerList?.find { it.first == "assist" }?.second?.apply {
            if (!players.containsKey(jerseyNumber) && jerseyNumber != null) {
                players.put(
                    jerseyNumber, PlayerUi(
                        name = fullName ?: "",
                        number = jerseyNumber,
                    )
                )
            }
            val locPercent = event.location?.toPercent() ?: Pair(0f, 0f)
            jerseyNumber?.let {
                players[it]?.apply {
                    plays.add(
                        PlayUi(
                            PlayType.ASSIST,
                            locPercent.first,
                            locPercent.second
                        )
                    )
                    stats[StatType.ASSISTS]?.apply {
                        count ++
                    }
                }
            }
        }

        playerList?.find { it.first == "rebound" }?.second?.apply {
            if (!players.containsKey(jerseyNumber) && jerseyNumber != null) {
                players.put(
                    jerseyNumber, PlayerUi(
                        name = fullName ?: "",
                        number = jerseyNumber,
                    )
                )
            }

            val locPercent = event.location?.toPercent() ?: Pair(0f, 0f)
            jerseyNumber?.let {
                players[it]?.apply {
                    plays.add(
                        PlayUi(
                            PlayType.ASSIST,
                            locPercent.first,
                            locPercent.second
                        )
                    )
                    stats[StatType.REBOUNDS]?.apply {
                        count ++
                    }
                }
            }
        }

        playerList?.find { it.first == "steal" }?.second?.apply {
            if (!players.containsKey(jerseyNumber) && jerseyNumber != null) {
                players.put(
                    jerseyNumber, PlayerUi(
                        name = fullName ?: "",
                        number = jerseyNumber,
                    )
                )
            }

            val locPercent = event.location?.toPercent() ?: Pair(0f, 0f)
            jerseyNumber?.let {
                players[it]?.apply {
                    plays.add(
                        PlayUi(
                            PlayType.ASSIST,
                            locPercent.first,
                            locPercent.second
                        )
                    )
                    stats[StatType.STEALS]?.apply {
                        count ++
                    }
                }
            }
        }

        playerList?.find { it.first == "freethrow" }?.second?.apply {
            if (!players.containsKey(jerseyNumber) && jerseyNumber != null) {
                players.put(
                    jerseyNumber, PlayerUi(
                        name = fullName ?: "",
                        number = jerseyNumber,
                    )
                )
            }


            if(event.eventType == EventType.freethrowmade.name) {
                jerseyNumber?.let {
                    players[it]?.apply {
                        stats[StatType.POINTS]?.apply {
                            count++
                        }
                    }
                }
            }
        }
    }

    private fun Location.toPercent(): Pair<Float, Float> {
        val halfCourtX = COURT_X_SIZE * 0.5f
        val x = coordX ?: 0
        val y = coordY ?: 0
        return when {
            x > halfCourtX -> Pair(
                (COURT_X_SIZE - x).toFloat() / halfCourtX,
                (COURT_Y_SIZE - y).toFloat() / COURT_Y_SIZE
            )

            else -> Pair(
                x.toFloat() / halfCourtX,
                y.toFloat() / COURT_Y_SIZE
            )
        }
    }


    companion object {
        const val COURT_X_SIZE = 1128
        const val COURT_Y_SIZE = 600
    }
}