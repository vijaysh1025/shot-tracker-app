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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
class PlayByPlayActivity : AppCompatActivity() {

    private val dailyScheduleViewModel by viewModels<DailyScheduleViewModel>()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShottrackerappTheme {
                // A surface container using the 'background' color from the theme

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