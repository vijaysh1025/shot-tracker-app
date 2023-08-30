package com.shottracker.feature.home.data.remote

import android.content.Context
import com.example.shot_tracker_app.R
import com.shottracker.feature.home.data.PlayByPlayDataSource
import com.shottracker.feature.home.models.pbp.PlayByPlay
import com.shottracker.network.Result
import com.shottracker.utils.ResUtil
import dagger.hilt.android.qualifiers.ApplicationContext

class PlayByPlayRemoteDataSource(
    private val nbaDataService: NbaDataService,
    @ApplicationContext private val context: Context
) : PlayByPlayDataSource {

    override suspend fun getPlayByPlay(gameId: String): Result<PlayByPlay> {
        return mockNetworkResponse()
    }

    private fun mockNetworkResponse(): Result<PlayByPlay> {
        val schedule = ResUtil.fromRawJson(
            PlayByPlay::class.java,
            context,
            R.raw.warriors_vs_suns_2021_12_03
        )

        schedule?.let {
            return Result.Success(schedule)
        }

        return Result.Failure(Result.Failure.DB_FAILED_TO_READ, "Mock response failed")
    }
}