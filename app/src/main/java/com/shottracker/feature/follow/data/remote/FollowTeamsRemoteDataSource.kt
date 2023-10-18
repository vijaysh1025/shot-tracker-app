package com.shottracker.feature.follow.data.remote

import com.example.shot_tracker_app.R
import com.shottracker.feature.follow.data.FollowTeamsDataSource
import com.shottracker.feature.follow.models.FollowTeam
import com.shottracker.feature.home.data.remote.NbaDataService
import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.feature.home.models.Hierarchy
import com.shottracker.network.Result
import com.shottracker.utils.ResUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import java.time.LocalDate

class FollowTeamsRemoteDataSource(
    private val nbaDataService: NbaDataService,
    @ApplicationContext private val context: ApplicationContext
) : FollowTeamsDataSource{

    // TODO: Will be used when we store followed teams remotely
    override suspend fun getFollowedTeams(): Result<List<FollowTeam>>? {
        return null
    }

    /**
     * Fetch all teams from SportRadar API endpoint
     */
    override suspend fun getAllTeams(): Result<List<FollowTeam>> {
        val result = nbaDataService.getLeagueHierarchy()
        return when(result) {
            is Result.Failure -> result
            is Result.NetworkError -> result
            is Result.Success -> {
                val teams = mutableListOf<FollowTeam>()
                result.data?.conferences?.forEach { conference ->
                    conference?.divisions?.forEach { division ->
                        division?.teams?.forEach { t ->
                            t?.let {
                                if(it?.id != null && it?.name != null && it?.alias != null && division?.alias != null) {
                                    teams.add(
                                        FollowTeam(
                                            id = it.id,
                                            name = it.name,
                                            alias = it.alias,
                                            division = division.alias
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
                Result.Success(teams)
            }
        }
    }

    // TODO: Will be implemented when we save followed teams for user remotely
    override suspend fun saveFollowedTeams(teams: List<FollowTeam>) {

    }

    private fun mockNetworkResponse(date: LocalDate): Result<Hierarchy> {
        val hierarchy = ResUtil.fromRawJson(
            Hierarchy::class.java,
            context,
            R.raw.league_heirarchy
        )

        hierarchy?.let {
            return Result.Success(hierarchy)
        }

        return Result.Failure(Result.Failure.DB_FAILED_TO_READ, "Mock response failed")
    }

}