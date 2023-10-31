package com.shottracker.feature.follow.data.local

import com.shottracker.feature.follow.data.FollowTeamsDataSource
import com.shottracker.feature.follow.models.FollowTeam
import com.shottracker.feature.home.data.DailyScheduleDataSource
import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class FollowTeamLocalDataSource internal constructor(
    private val followTeamsDao: FollowTeamsDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : FollowTeamsDataSource {
    override suspend fun getFollowedTeams(): Result<List<FollowTeam>> = withContext(ioDispatcher) {
        var followedTeams: List<FollowTeam>? = null
        try {
            followedTeams = followTeamsDao.getFollowedTeams()
        } catch (e: Exception) {
            //TODO: Log failure
        }
        followedTeams?.let {
            return@withContext Result.Success(it)
        }
        return@withContext Result.Failure(Result.Failure.DB_FAILED_TO_READ)
    }

    override suspend fun getAllTeams(): Result<List<FollowTeam>> = withContext(ioDispatcher) {
        var allTeams: List<FollowTeam>? = null
        try {
            allTeams = followTeamsDao.getAllTeams()
        } catch (e: Exception) {
            //TODO: Log failure
        }
        allTeams?.let {
            return@withContext Result.Success(it)
        }
        return@withContext Result.Failure(Result.Failure.DB_FAILED_TO_READ)
    }

    override suspend fun saveFollowedTeams(teams: List<FollowTeam>) = withContext(ioDispatcher) {
        followTeamsDao.saveFollowedTeams(teams)
    }

}