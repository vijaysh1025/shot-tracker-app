package com.shottracker.feature.follow.models

data class FollowTeam(
    val id:String,
    val name:String,
    val alias:String,
    val division: String,
    var isFollowed: Boolean = false
)

