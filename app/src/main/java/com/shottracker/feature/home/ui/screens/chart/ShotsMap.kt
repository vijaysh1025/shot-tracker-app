package com.shottracker.feature.home.ui.screens.chart

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.shot_tracker_app.R
import com.shottracker.feature.home.ui.models.PlayType
import com.shottracker.feature.home.ui.models.PlayUi
import com.shottracker.feature.home.ui.models.PlayerUi
import com.shottracker.ui.theme.CourtArc
import com.shottracker.ui.theme.CourtColor
import com.shottracker.ui.theme.CourtLines

@Preview
@Composable
fun Court(isVisible: Boolean = false) {
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

private class TransCoords(
    xPos: State<Float>,
    yPos: State<Float>,
    morph: State<Float>
) {
    val xPos by xPos
    val yPos by yPos
    val morph by morph
}