package com.shottracker.feature.home.ui.screens.chart

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shottracker.feature.home.ui.models.PlayerUi
import com.shottracker.feature.home.ui.models.TeamUi
import com.shottracker.feature.home.ui.screens.LoadingScreen
import com.shottracker.feature.home.ui.screens.RetryErrorScreen
import com.shottracker.feature.home.ui.states.PbpUiState
import com.shottracker.feature.home.viewmodel.PlayByPlayViewModel
import com.shottracker.ui.theme.Purple200


@Composable
fun ChartScreen(viewModel: PlayByPlayViewModel) {
    val uiState = viewModel.pbpUiState.observeAsState(initial = PbpUiState.Loading)
    val playerState = viewModel.selectedPlayer.observeAsState()
    val teamState = viewModel.selectedTeam.observeAsState()
    val showCourt = viewModel.showCourt.observeAsState()

    val pickTeam = { teamUi: TeamUi ->
        viewModel.selectTeam(teamUi)
    }

    val pickPlayer = { playerUi: PlayerUi ->
        Log.d("SELECT", playerUi.name)
        viewModel.selectPlayer(playerUi)
    }

    Surface(modifier = Modifier.background(Color.Blue)) {
        when (uiState.value) {
            PbpUiState.ErrorRetry -> RetryErrorScreen()
            PbpUiState.Loading -> LoadingScreen()
            is PbpUiState.Success -> {
                val homeTeam = (uiState.value as PbpUiState.Success).homeUi
                val awayTeam = (uiState.value as PbpUiState.Success).awayUi
                SuccessScreen(
                    playerState,
                    teamState,
                    homeTeam,
                    awayTeam,
                    pickPlayer,
                    pickTeam,
                    showCourt.value ?: false
                )
            }
        }
    }
}


@Composable
private fun SuccessScreen(
    playerState: State<PlayerUi?>,
    teamState: State<TeamUi?>,
    awayTeam: TeamUi,
    homeTeam: TeamUi,
    selectPlayer: (PlayerUi) -> Unit,
    selectTeam: (TeamUi) -> Unit,
    showCourt: Boolean = false
) {
    val colorStops = arrayOf(
        0.0f to Color.Transparent,
        1.0f to Purple200
    )
    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(TopCenter)
        ) {
            Court(showCourt)
            playerState.value?.let {
                ShotsMap(playerUi = it)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(BottomCenter)
                .background(Brush.verticalGradient(colorStops = colorStops))
                .padding(bottom = 16.dp, top = 24.dp),
        ) {
            playerState.value?.let {
                StatsChart(it)
            }
            teamState.value?.players?.let {
                PlayerPicker(players = it.values.toList(), playerState.value, selectPlayer)
            }
            TeamPicker(teams = listOf(homeTeam, awayTeam), teamState.value, selectTeam)
        }
    }
}









