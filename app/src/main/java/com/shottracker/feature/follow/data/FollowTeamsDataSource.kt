package com.shottracker.feature.follow.data

import com.shottracker.feature.follow.models.FollowTeam
import com.shottracker.network.Result

interface FollowTeamsDataSource {
    suspend fun getFollowedTeams() : Result<List<FollowTeam>>?

    suspend fun getAllTeams() : Result<List<FollowTeam>>

    suspend fun saveFollowedTeams(teams:List<FollowTeam>)
}