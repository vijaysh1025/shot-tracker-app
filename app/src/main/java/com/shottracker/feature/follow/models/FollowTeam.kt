package com.shottracker.feature.follow.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "followteams")
data class FollowTeam(

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id:String,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "alias")
    val alias:String,

    @ColumnInfo(name = "division")
    val division: String,

    @ColumnInfo(name = "isFollowed")
    var isFollowed: Boolean = false
)

