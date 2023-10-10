package com.shottracker.feature.home.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.shottracker.feature.home.ui.screens.chart.ChartScreen
import com.shottracker.feature.home.ui.screens.GamesScreen
import com.shottracker.feature.home.viewmodel.DailyScheduleViewModel
import com.shottracker.feature.home.viewmodel.PlayByPlayViewModel
import com.shottracker.ui.theme.ShottrackerappTheme
import dagger.hilt.android.AndroidEntryPoint


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



