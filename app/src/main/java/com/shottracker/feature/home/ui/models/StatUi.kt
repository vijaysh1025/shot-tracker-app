package com.shottracker.feature.home.ui.models

data class StatUi(
    val type:StatType,
    var count:Int = 0,
    val maxCount:Int = 0
)

data class PlayerUi(
    val name:String = "",
    val number:String = "",
    val plays: MutableList<PlayUi> = mutableListOf(),
    val stats: Map<StatType, StatUi> = mapOf(
        StatType.POINTS to StatUi(StatType.POINTS, 0, 80),
        StatType.REBOUNDS to StatUi(StatType.REBOUNDS, 0, 40),
        StatType.STEALS to StatUi(StatType.STEALS, 0, 10),
        StatType.ASSISTS to StatUi(StatType.ASSISTS, 0, 40),
    )
)

data class TeamUi(
    val id:String = "",
    val name:String = "",
    val alias:String = "",
    val players:MutableMap<String, PlayerUi> = mutableMapOf()
)

data class PlayUi(
    val type: PlayType,
    val xLoc: Float,
    val yLoc: Float
)

enum class PlayType{
    SHOT_MADE,
    SHOT_MISSED,
    REBOUND,
    ASSIST,
    STEAL
}

enum class StatType(name:String) {
    POINTS("Points"),
    REBOUNDS("Rebounds"),
    STEALS("Steals"),
    ASSISTS("Assists")
}

