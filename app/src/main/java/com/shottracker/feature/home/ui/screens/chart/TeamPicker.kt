package com.shottracker.feature.home.ui.screens.chart

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shottracker.feature.home.ui.models.PlayerUi
import com.shottracker.feature.home.ui.models.TeamUi

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
