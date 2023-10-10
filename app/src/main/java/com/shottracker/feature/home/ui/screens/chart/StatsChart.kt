package com.shottracker.feature.home.ui.screens.chart

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateInt
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.shottracker.feature.home.ui.models.PlayerUi
import com.shottracker.feature.home.ui.models.StatUi


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
                modifier = Modifier.wrapContentSize().align(Alignment.Center),
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
                .align(Alignment.CenterVertically),
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
                .align(Alignment.CenterVertically),
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