package com.shottracker.feature.home.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shottracker.ui.components.DatePickerTimeline
import com.shottracker.feature.home.models.GamesItem
import com.shottracker.feature.home.ui.screens.ChartScreen
import com.shottracker.feature.home.ui.screens.GamesScreen
import com.shottracker.feature.home.ui.states.DsUiState
import com.shottracker.feature.home.viewmodel.DailyScheduleViewModel
import com.shottracker.feature.home.viewmodel.PlayByPlayViewModel
import com.shottracker.ui.components.DatePickerState
import com.shottracker.ui.theme.ShottrackerappTheme
import com.shottracker.utils.ResUtil
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.util.*


/**
 * This Activity is the Entry point to this app.
 */
@ExperimentalAnimationApi
@AndroidEntryPoint
class HomeActivity : ComponentActivity() {

    private val dailyScheduleViewModel by viewModels<DailyScheduleViewModel>()
    private val playByPlayViewModel by viewModels<PlayByPlayViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShottrackerappTheme {
                // A surface container using the 'background' color from the theme
                Application(dailyScheduleViewModel = dailyScheduleViewModel, playViewModel = playByPlayViewModel)
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Application(dailyScheduleViewModel: DailyScheduleViewModel, playViewModel: PlayByPlayViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "main" ) {
        composable("main") {
            GamesScreen(viewModel = dailyScheduleViewModel) {
                it?.let {
                    navController.navigate("chart")
                    playViewModel.fetchPlayByPlay(it)
                }
            }
        }
        composable("chart") {
            ChartScreen(viewModel = playViewModel)
        }
    }
}


