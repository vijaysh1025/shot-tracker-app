package com.shottracker.feature.home.ui.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.map
import com.example.shot_tracker_app.R
import com.shottracker.feature.home.models.GamesItem
import com.shottracker.feature.home.ui.states.DsUiState
import com.shottracker.feature.home.viewmodel.DailyScheduleViewModel
import com.shottracker.ui.components.DatePickerState
import com.shottracker.ui.components.DatePickerTimeline
import com.shottracker.ui.theme.Purple200
import com.shottracker.ui.theme.Purple500
import com.shottracker.ui.theme.VsColor
import com.shottracker.utils.ResUtil
import java.time.LocalDate

/**
 * This is the Main Compose screen for this activity
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@ExperimentalAnimationApi
@Composable
fun GamesScreen(viewModel: DailyScheduleViewModel, onSettingsClick: ()->Unit = {}, onGameSelected:(String?) -> Unit) {
    val uiState = viewModel.dsUiState.observeAsState(initial = DsUiState.Loading)
    val dateState = viewModel.datePicked.map { DatePickerState(it) }.observeAsState(
        DatePickerState(
            LocalDate.now())
    )
    val datePicked:(LocalDate)->Unit = {
        Log.d("DATE_PICKED", "$it")
        viewModel.dispatchDatePicked(it)
    }

    Scaffold (
        modifier = Modifier.fillMaxSize().background(Purple200),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Daily Games")
                },
                navigationIcon = {
                    IconButton( onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.vd_vector),
                            contentDescription = "Go back"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsClick) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Purple500,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) {
        Surface(
            color = Color.LightGray,
            modifier = Modifier.fillMaxSize().padding(it)
        ) {
            when (uiState.value) {
                DsUiState.ErrorRetry -> RetryErrorScreen()
                is DsUiState.Games -> GamesListScreen(
                    games = (uiState.value as DsUiState.Games).games,
                    onGameSelected = onGameSelected
                )

                DsUiState.Loading -> LoadingScreen()
            }
            if (uiState.value !is DsUiState.ErrorRetry) {
                Box(modifier = Modifier.fillMaxSize()) {
                    DatePickerTimeline(
                        modifier = Modifier
                            .wrapContentHeight()
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        onDateSelected = datePicked,
                        state = dateState.value
                    )
                }
            }
        }
    }
}


/**
 * This is the retry screen that should appear if
 * Daily Schedule cannot be fetched from repo (local and remote failure)
 */
@Composable
fun RetryErrorScreen() {
    Surface(
        color = Color.White,
        modifier = Modifier.fillMaxSize()
    ) {
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Retry")
        }
    }
}

/**
 * This screen will show when Daily Schedule is being loaded from repo
 */
@Composable
fun LoadingScreen() {
    Surface(
        color = Color.LightGray,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.wrapContentSize(
                align = Alignment.Center
            )
        )
    }
}

/**
 * This screen shows the loaded list of games for a given date.
 */
@ExperimentalAnimationApi
@Composable
fun GamesListScreen(
    games: List<GamesItem>,
    onGameSelected: (String?) -> Unit
) {
    var n = 0
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(games) { game ->
                GameCard(game = game, n++, onGameSelected)
            }
        }
    }
}

/**
 * This is an individual card shown for a specific game.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameCard(game: GamesItem, index: Int, onGameSelected: (String?) -> Unit) {
    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(delayMillis = index * 100)) + slideInVertically(animationSpec = tween(delayMillis = index*100)) { it + 20}
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            shape = RoundedCornerShape(50),
            onClick = {
                onGameSelected(game.id)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .align(alignment = Alignment.CenterStart)
                ) {
                    Image(
                        painter = painterResource(
                            ResUtil.loadIcon(
                                game.home?.alias,
                                LocalContext.current
                            )
                        ),
                        contentDescription = "home",
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.CenterVertically)
                            .padding(10.dp)
                    )
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = game.home?.alias ?: "NA",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = game.homePoints?.toString() ?: "--",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if ((game.homePoints ?: 0) > (game.awayPoints ?: 0)) Color.Black else Color.Gray
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .clip(CircleShape)
                        .align(alignment = Alignment.Center)
                ) {
                    Text(
                        text = "VS",
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.White,
                        modifier = Modifier
                            .background(VsColor)
                            .padding(10.dp)
                    )
                }
                Row(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight()
                        .align(alignment = Alignment.CenterEnd)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = game.away?.alias ?: "NA",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = game.awayPoints?.toString() ?: "--",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if ((game.awayPoints ?: 0) > (game.homePoints ?: 0)) Color.Black else Color.Gray
                        )
                    }
                    Image(
                        painter = painterResource(
                            ResUtil.loadIcon(
                                game.away?.alias,
                                LocalContext.current
                            )
                        ),
                        contentDescription = "home",
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.CenterVertically)
                            .padding(10.dp)
                    )
                }
            }
        }
    }
}