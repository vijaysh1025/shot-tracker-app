package com.shottracker.feature.follow.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shottracker.feature.follow.models.FollowTeam
import com.shottracker.feature.home.models.DailySchedule

@Dao
interface FollowTeamsDao {
    @Query("SELECT * FROM followteams where isFollowed = 1 ")
    suspend fun getFollowedTeams() : List<FollowTeam>

    @Query("SELECT * FROM followteams")
    suspend fun getAllTeams() : List<FollowTeam>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFollowedTeams(teams: List<FollowTeam>)

}