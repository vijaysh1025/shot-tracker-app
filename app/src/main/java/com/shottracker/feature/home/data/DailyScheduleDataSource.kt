package com.shottracker.feature.home.data

import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.network.Result
import java.time.LocalDate
import java.util.*

interface DailyScheduleDataSource {

    suspend fun getGamesForDate(date: LocalDate) : Result<DailySchedule>

    suspend fun saveGames(dailySchedule: DailySchedule)

}