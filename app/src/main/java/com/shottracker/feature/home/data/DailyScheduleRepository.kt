package com.shottracker.feature.home.data

import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.network.Result
import java.time.LocalDate
import java.util.*

interface DailyScheduleRepository {

    suspend fun getGamesForDate(date: LocalDate): Result<DailySchedule>

}