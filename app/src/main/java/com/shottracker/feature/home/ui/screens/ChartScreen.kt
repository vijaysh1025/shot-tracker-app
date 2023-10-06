package com.shottracker.feature.home.ui.screens

import android.graphics.drawable.AnimatedImageDrawable
import android.graphics.drawable.AnimatedVectorDrawable
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.TopCenter
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.LinearGradientShader
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shot_tracker_app.R
import com.shottracker.feature.home.models.pbp.Team
import com.shottracker.feature.home.ui.models.PlayType
import com.shottracker.feature.home.ui.models.PlayUi
import com.shottracker.feature.home.ui.models.PlayerUi
import com.shottracker.feature.home.ui.models.StatType
import com.shottracker.feature.home.ui.models.StatUi
import com.shottracker.feature.home.ui.models.TeamUi
import com.shottracker.feature.home.ui.states.PbpUiState
import com.shottracker.feature.home.viewmodel.PlayByPlayViewModel
import com.shottracker.ui.theme.CourtArc
import com.shottracker.ui.theme.CourtColor
import com.shottracker.ui.theme.CourtLines
import com.shottracker.ui.theme.Purple200
import com.shottracker.ui.theme.Teal200
import org.xmlpull.v1.XmlPullParser


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

@Preview
@Composable
private fun Court(isVisible: Boolean = false) {
    val colorStops = arrayOf(
        0.0f to CourtColor,
        0.7f to CourtColor,
        1.0f to Color.Transparent
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(Brush.verticalGradient(colorStops = colorStops))
            .padding(start = 4.dp, top = 30.dp, end = 4.dp, bottom = 40.dp)
            .border(
                2.dp,
                CourtLines
            )
    ) {
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter), visible = isVisible, enter = scaleIn()
        ) {
            Box(
                modifier = Modifier
                    .padding(bottom = 100.dp)
                    .fillMaxWidth()
                    .height(280.dp)
                    .padding(start = 30.dp, end = 30.dp, bottom = 30.dp)
                    .border(
                        2.dp,
                        CourtLines,
                        shape = RoundedCornerShape(0.dp, 0.dp, 200.dp, 200.dp)
                    )
                    .clip(RoundedCornerShape(0.dp, 0.dp, 200.dp, 200.dp))
                    .background(Color.LightGray)
            )
        }
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.TopCenter),
            visible = isVisible,
            enter = slideIn(animationSpec = tween(
                durationMillis = 300, delayMillis = 200, easing = LinearOutSlowInEasing
            ), initialOffset = { IntOffset(0, -700) })
        ) {
            Box(
                modifier = Modifier
                    .height(190.dp)
                    .width(120.dp)
                    .border(
                        2.dp,
                        CourtLines,
                        shape = RoundedCornerShape(0.dp, 0.dp, 200.dp, 200.dp)
                    )
                    .clip(RoundedCornerShape(0.dp, 0.dp, 200.dp, 200.dp))
                    .background(CourtArc)
            ) {
                AnimatedVisibility(
                    modifier = Modifier.align(Alignment.BottomCenter),
                    visible = isVisible,
                    enter = scaleIn(
                        animationSpec = tween(
                            durationMillis = 300, delayMillis = 1500, easing = EaseOut
                        )
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .padding(bottom = 60.dp)
                            .border(
                                2.dp,
                                CourtLines,
                                shape = RoundedCornerShape(200.dp, 200.dp, 0.dp, 0.dp)
                            )
                            .width(120.dp)
                            .height(60.dp)

                    )
                }
            }
        }
    }
}

@Composable
fun ShotsMap(playerUi: PlayerUi) {
    Box(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 30.dp)
            .fillMaxWidth()
            .height(400.dp)
    ) {
        playerUi.plays.filter { it.type == PlayType.SHOT_MADE || it.type == PlayType.SHOT_MISSED }
            .forEach {
                Shot(it)
            }
    }
}

@Composable
private fun Shot(play: PlayUi) {
    val coordinate = animateCoords(play = play)
    val inifiniteTransition = rememberInfiniteTransition()
    val sizeAnim by inifiniteTransition.animateFloat(
        initialValue = 15f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = EaseOut),
            repeatMode = RepeatMode.Reverse
        ), label = "size Anim"
    )

    Box {
        Icon(
            painter = painterResource(id = R.drawable.made),
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        val xPos = coordinate.yPos * constraints.maxWidth
                        val yPos = coordinate.xPos * constraints.maxHeight
                        placeable.placeRelative(xPos.toInt(), yPos.toInt())
                    }
                }
                .size(sizeAnim.dp)
                .offset((-sizeAnim * 0.5f).dp, (-sizeAnim * 0.5f).dp)
                .scale(coordinate.morph),
            contentDescription = "Made"
        )
        Icon(
            painter = painterResource(id = R.drawable.missed),
            modifier = Modifier
                .layout { measurable, constraints ->
                    val placeable = measurable.measure(constraints)
                    layout(constraints.maxWidth, constraints.maxHeight) {
                        val xPos = coordinate.yPos * constraints.maxWidth
                        val yPos = coordinate.xPos * constraints.maxHeight
                        placeable.placeRelative(xPos.toInt(), yPos.toInt())
                    }
                }
                .size(15.dp)
                .scale(1f - coordinate.morph),
            contentDescription = "Made"
        )
    }
}


@Composable
fun ColumnScope.StatsChart(playerUi: PlayerUi) {
    Row (
        modifier = Modifier
            .wrapContentSize()
            .padding(20.dp)
            .align(Alignment.CenterHorizontally)
    ) {
        playerUi.stats.forEach {
            StatCircle(statUi = it.value)
        }
    }
}


@Composable
fun Chip(
    player: PlayerUi,
    isSelected: Boolean = false,
    onSelectionChanged: (PlayerUi) -> Unit,
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(40.dp))
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(player)
                }
            ),
        shape = RoundedCornerShape(40.dp),
        color = if (isSelected) Color.DarkGray else Color.White
    ) {
        Box {
            Text(
                text = player.name,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else Color.Black,
                modifier = Modifier.padding(12.dp, 8.dp)
            )
        }
    }
}

@Composable
fun Chip(
    team: TeamUi,
    isSelected: Boolean = false,
    onSelectionChanged: (TeamUi) -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .border(1.dp, color = Color.Black, shape = RoundedCornerShape(40.dp)),
        shape = RoundedCornerShape(40.dp),
        color = if (isSelected) Color.DarkGray else Color.White
    ) {
        Row(modifier = Modifier
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelectionChanged(team)
                }
            )
        ) {
            Text(
                text = team.name,
                style = MaterialTheme.typography.labelLarge,
                color = if (isSelected) Color.White else Color.Black,
                modifier = Modifier.padding(12.dp, 8.dp)
            )
        }
    }
}


fun getTeam(): List<TeamUi> = listOf(
    TeamUi("Warriors"),
    TeamUi("Heat")
)


@Composable
fun PlayerPicker(
    players: List<PlayerUi>,
    selectedPlayer: PlayerUi? = null,
    onSelectedChanged: (PlayerUi) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        LazyRow {
            items(players) {
                Chip(
                    player = it,
                    isSelected = selectedPlayer?.name == it.name,
                    onSelectionChanged = onSelectedChanged
                )
            }
        }
    }
}

@Composable
fun ColumnScope.TeamPicker(
    teams: List<TeamUi>,
    selectedTeam: TeamUi? = null,
    onSelectedChanged: (TeamUi) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .align(Alignment.CenterHorizontally)
    ) {
        for (t in teams) {
            Chip(
                team = t,
                isSelected = selectedTeam == t,
                onSelectionChanged = onSelectedChanged
            )
        }
    }

}

@Composable
private fun StatCircle(statUi: StatUi) {
    val transData = animateStat(stat = statUi)
    Column (modifier = Modifier.wrapContentSize()) {
        Box(modifier = Modifier.size(80.dp).padding(8.dp)) {
            CircularProgressIndicator(
                progress = transData.fill,
                modifier = Modifier.fillMaxSize(),
                color = Color.DarkGray,
                trackColor = Color.White,
                strokeWidth = 8.dp
            )
            Text(
                modifier = Modifier.wrapContentSize().align(Center),
                text = transData.count.toString(),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
        }
        Text(
            modifier = Modifier.wrapContentSize().align(Alignment.CenterHorizontally),
            text = statUi.type.name,
            fontSize = 12.sp,
            color = Color.DarkGray,
        )
    }
}

@Composable
private fun StatBar(stat: StatUi) {

    val transData = animateStat(stat = stat)
    Row(
        modifier = Modifier
            .padding(2.dp)
            .fillMaxWidth()
            .height(30.dp)
    ) {
        Text(
            modifier = Modifier
                .wrapContentHeight()
                .width(80.dp)
                .align(CenterVertically),
            text = stat.type.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End
        )
        Spacer(modifier = Modifier.width(10.dp))
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(transData.fill)
                .background(Color.Black)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            modifier = Modifier
                .wrapContentSize()
                .align(CenterVertically),
            text = transData.count.toString(),
            style = MaterialTheme.typography.titleMedium
        )
    }
}

private class TransData(
    color: State<Color>,
    fillPercent: State<Float>,
    count: State<Int>
) {
    val color by color
    val fill by fillPercent
    val count by count
}

@Composable
private fun animateStat(stat: StatUi): TransData {
    val transition = updateTransition(targetState = stat, label = "stat state")
    val color = transition.animateColor(label = "color") {
        when {
            it.count > 5 -> Color.Black
            else -> Color.DarkGray
        }
    }
    val size = transition.animateFloat(label = "size") {
        it.count.toFloat() / it.maxCount.toFloat()
    }
    val count = transition.animateInt(label = "count") {
        it.count
    }
    return remember(transition) { TransData(color, size, count) }
}


private class TransCoords(
    xPos: State<Float>,
    yPos: State<Float>,
    morph: State<Float>
) {
    val xPos by xPos
    val yPos by yPos
    val morph by morph
}

@Composable
private fun animateCoords(play: PlayUi): TransCoords {
    val transition = updateTransition(targetState = play, label = "stat state")

    val coordX = transition.animateFloat(label = "coordX") {
        it.xLoc
    }

    val coordY = transition.animateFloat(label = "coordX") {
        it.yLoc
    }

    val morph = transition.animateFloat(label = "morph") {
        when (it.type) {
            PlayType.SHOT_MADE -> 1.0f
            PlayType.SHOT_MISSED -> 0.0f
            else -> 0.0f
        }
    }

    return remember(transition) { TransCoords(coordX, coordY, morph) }
}
