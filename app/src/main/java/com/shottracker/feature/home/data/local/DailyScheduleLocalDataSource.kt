package com.shottracker.feature.home.data.local

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

class DailyScheduleLocalDataSource internal constructor(
    private val dailyScheduleDao: DailyScheduleDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DailyScheduleDataSource {
    override suspend fun getGamesForDate(date: LocalDate): Result<DailySchedule> =
        withContext(ioDispatcher) {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val dateId = ""
            var dailySchedule:DailySchedule? = null
            try {
                dailySchedule = dailyScheduleDao.getGamesForDate(dateId)
            } catch (e: Exception) {
                //TODO: Log failure
            }
            dailySchedule?.let {
                return@withContext Result.Success(it)
            }
            return@withContext Result.Failure(Result.Failure.DB_FAILED_TO_READ)
        }

    override suspend fun saveGames(dailySchedule: DailySchedule) = withContext(ioDispatcher) {
        dailyScheduleDao.saveGames(dailySchedule)
    }
}