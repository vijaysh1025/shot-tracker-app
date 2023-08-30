package com.shottracker.feature.home.data

import com.shottracker.feature.home.models.pbp.PlayByPlay
import com.shottracker.network.Result

interface PlayByPlayDataSource {
    suspend fun getPlayByPlay(gameId: String) : Result<PlayByPlay>

}