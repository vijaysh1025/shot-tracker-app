package com.shottracker.feature.home.ui

import android.os.Build
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.shottracker.feature.home.models.GamesItem
import com.shottracker.feature.home.ui.states.DatePickerState
import com.shottracker.feature.home.ui.states.DsUiState
import com.shottracker.feature.home.viewmodel.DailyScheduleViewModel
import com.shottracker.ui.theme.ShottrackerappTheme
import com.shottracker.utils.ResUtil
import com.shottracker.utils.anim.AnimatingCalendarBox
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


/**
 * This Activity is the Entry point to this app.
 */
@ExperimentalAnimationApi
@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val dailyScheduleViewModel by viewModels<DailyScheduleViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShottrackerappTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainScreen(viewModel = dailyScheduleViewModel)
                }
            }
        }
    }
}

/**
 * This is the Main Compose screen for this activity
 */
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun MainScreen(viewModel: DailyScheduleViewModel) {
    val uiState = viewModel.dsUiState.observeAsState(initial = DsUiState.Loading)
    val datePickerState =
        viewModel.datePickerState.observeAsState(initial = DatePickerState.Collapsed)
    val dateState = viewModel.datePicked.observeAsState(Calendar.getInstance().time)
    val dateButtonClicked = {
        val nextState = when (viewModel.datePickerState.value) {
            DatePickerState.Collapsed -> DatePickerState.Expanded
            DatePickerState.Expanded -> DatePickerState.Collapsed
            else -> DatePickerState.Collapsed
        }
        viewModel.dispatchDatePickerState(nextState)
    }

    val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    val datePicked:(Date)->Unit = {
        viewModel.dispatchDatePickerState(DatePickerState.Collapsed)
        viewModel.dispatchDatePicked(it)
    }

    Surface(
        color = Color.LightGray,
        modifier = Modifier.fillMaxSize()
    ) {
        when (uiState.value) {
            DsUiState.ErrorRetry -> RetryErrorScreen()
            is DsUiState.Games -> GamesListScreen(
                games = (uiState.value as DsUiState.Games).games
            )
            DsUiState.Loading -> LoadingScreen()
        }
        if(uiState.value !is DsUiState.ErrorRetry) {
            Box(modifier = Modifier.fillMaxSize()) {
                AnimatingCalendarBox(
                    Modifier.align(Alignment.BottomCenter),
                    datePickerState.value,
                    dateButtonClicked,
                    datePicked,
                    dateFormat.format(dateState.value)
                )
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
        color = Color.White,
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
@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun GamesListScreen(
    games: List<GamesItem>
) {
    var n = 0
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn {
            items(games) { game ->
                GameCard(game = game, n++)
            }
        }
    }
}

/**
 * This is an individual card shown for a specific game.
 */
@ExperimentalAnimationApi
@Composable
fun GameCard(game: GamesItem, index: Int) {
    val state = remember {
        MutableTransitionState(false).apply {
            // Start the animation immediately.
            targetState = true
        }
    }
    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(animationSpec = tween(delayMillis = index * 100)) + slideInVertically(
            { it },
            animationSpec = tween(delayMillis = index * 100)
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(10.dp),
            shape = RoundedCornerShape(50)
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
                            text = game.away?.alias ?: "NA",
                            style = MaterialTheme.typography.h6,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = game.homePoints?.toString() ?: "--",
                            style = MaterialTheme.typography.h4,
                            color = if (game.homePoints ?: 0 > game.awayPoints ?: 0) Color.Black else Color.Gray
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
                        style = MaterialTheme.typography.h6,
                        color = Color.White,
                        modifier = Modifier
                            .background(Color.DarkGray)
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
                            text = game.home?.alias ?: "NA",
                            style = MaterialTheme.typography.h6,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.CenterHorizontally)
                        )
                        Text(
                            text = game.awayPoints?.toString() ?: "--",
                            style = MaterialTheme.typography.h4,
                            color = if (game.awayPoints ?: 0 > game.homePoints ?: 0) Color.Black else Color.Gray
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

@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShottrackerappTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {

        }
    }
}