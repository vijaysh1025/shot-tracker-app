package com.shottracker.feature.home.data.remote

import android.content.Context
import com.example.shot_tracker_app.R
import com.shottracker.feature.home.data.DailyScheduleDataSource
import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.network.Result
import com.shottracker.utils.ResUtil
import dagger.hilt.android.qualifiers.ApplicationContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class DailyScheduleRemoteDataSource(
    private val nbaDataService: NbaDataService,
    @ApplicationContext private val context: Context
) : DailyScheduleDataSource {

    private val pbpIds = listOf(
        R.raw.daily_schedule_2021_11_23,
        R.raw.daily_schedule_2021_11_24,
        R.raw.daily_schedule_2021_11_25
    )

    override suspend fun getGamesForDate(date: LocalDate): Result<DailySchedule> {
//        val dateFormat = SimpleDateFormat("yyyy-mm-dd", Locale.US)
//        val dateId = dateFormat.format(date)
//        val dateArray = dateId.split("-")

        return mockNetworkResponse(date)//nbaDataService.getDailySchedule(dateArray[0],dateArray[1],dateArray[2])
    }

    override suspend fun saveGames(dailySchedule: DailySchedule) {
    }

    private fun mockNetworkResponse(date: LocalDate): Result<DailySchedule> {
        val id = pbpIds[date.dayOfMonth % 3]

        val schedule = ResUtil.fromRawJson(
            DailySchedule::class.java,
            context,
            id
        )

        schedule?.let {
            return Result.Success(schedule)
        }

        return Result.Failure(Result.Failure.DB_FAILED_TO_READ, "Mock response failed")
    }
}