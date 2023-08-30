package com.shottracker.utils.anim

import android.os.Build
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.shottracker.feature.home.ui.states.DatePickerState
import java.util.*

enum class BoxState { Collapsed, Expanded }

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalAnimationApi
@Composable
fun AnimatingCalendarBox(
    modifier: Modifier,
    datePickerState: DatePickerState,
    dateButtonClicked: () -> Unit,
    datePicked: (Date) -> Unit,
    dateText: String
) {
    val transitionData = updateTransitionData(datePickerState)
    val state = remember {
        MutableTransitionState(datePickerState == DatePickerState.Expanded).apply {
            targetState = datePickerState == DatePickerState.Expanded
        }
    }
    // UI tree
    Box(
        modifier = modifier
            .width(transitionData.width)
            .height(transitionData.height)
            .padding(bottom = 20.dp)
            .clip(RoundedCornerShape(transitionData.corner))
    ) {
        when (datePickerState) {
            DatePickerState.Expanded -> AndroidView(
                factory = {
                    val datePicker = DatePicker(it)
                    datePicker.setOnDateChangedListener { _, i, i2, i3 ->
                        val cal = Calendar.getInstance()
                        cal.set(i, i2, i3)
                        datePicked(cal.time)
                    }

                    datePicker
                },
                modifier = Modifier
                    .wrapContentWidth()
                    .background(Color.White)
                    .align(Alignment.BottomCenter)

            )

            else -> Button(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                onClick = { dateButtonClicked() }) {
                Text(text = dateText)
            }
        }

    }
}

// Holds the animation values.
private class TransitionData(
    color: State<Color>,
    width: State<Dp>,
    height: State<Dp>,
    corner: State<Int>
) {
    val color by color
    val width by width
    val height by height
    val corner by corner
}

// Create a Transition and return its animation values.
@Composable
private fun updateTransitionData(datePickerState: DatePickerState): TransitionData {
    val transition = updateTransition(datePickerState, "update")
    val color = transition.animateColor(label = "colorAnim") { state ->
        when (state) {
            DatePickerState.Expanded -> Color.White
            DatePickerState.Collapsed -> Color.Black
            DatePickerState.Hidden -> Color.Black
        }
    }
    val width = transition.animateDp(label = "dpAnim") { state ->
        when (state) {
            DatePickerState.Hidden -> 0.dp
            DatePickerState.Collapsed -> 300.dp
            DatePickerState.Expanded -> LocalConfiguration.current.screenWidthDp.dp
        }
    }

    val height = transition.animateDp(label = "dpAnim") { state ->
        when (state) {
            DatePickerState.Hidden -> 0.dp
            DatePickerState.Collapsed -> 80.dp
            DatePickerState.Expanded -> LocalConfiguration.current.screenWidthDp.dp + 100.dp
        }
    }

    val corner = transition.animateInt(label = "dpAnim") { state ->
        when (state) {
            DatePickerState.Hidden -> 0
            DatePickerState.Collapsed -> 50
            DatePickerState.Expanded -> 5
        }
    }
    return remember(transition) { TransitionData(color, width, height, corner) }
}