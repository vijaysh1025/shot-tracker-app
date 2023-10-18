package com.shottracker.feature.home.data.remote

import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.feature.home.models.Hierarchy
import com.shottracker.feature.home.models.pbp.PlayByPlay
import com.shottracker.network.Result
import retrofit2.http.GET
import retrofit2.http.Path

interface NbaDataService {

    @GET("league/hierarchy.json")
    fun getLeagueHierarchy() : Result<Hierarchy>

    @GET("games/{year}/{month}/{day}/schedule.json")
    fun getDailySchedule(
        @Path("year") year: String,
        @Path("month") month: String,
        @Path("day") day: String
    ) : Result<DailySchedule>

    @GET("en/games/{game_id}/pbp.json")
    fun getPlayByPlay(
        @Path("game_id") gameId: String
    ) : Result<PlayByPlay>

    companion object {
        const val BASE_URL: String = "https://api.sportradar.us/nba/trial/v7/en/"
        const val API_KEY:String = "cyrbf3ed3fmwvsy3ddrnc889"
        const val Name = "NbaDataService"
    }
}