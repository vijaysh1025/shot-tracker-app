package com.shottracker.feature.home.data


import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.network.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.*

class DailyScheduleRepositoryImpl(
    private val localDataSource: DailyScheduleDataSource,
    private val remoteDataSource: DailyScheduleDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : DailyScheduleRepository {

    /**
     * Get games for give date.
     */
    override suspend fun getGamesForDate(date: LocalDate) : Result<DailySchedule> = withContext(ioDispatcher){
        // Fetch games for date from DB
        var result = localDataSource.getGamesForDate(date)

        // If DB has no data for specific date, fetch from API
        if(result is Result.Failure) {
            result = remoteDataSource.getGamesForDate(date)

            // If network Fetch is successful, store in DB
            if(result is Result.Success) {
                result.data?.let {
                    localDataSource.saveGames(it)
                }
            }
        }
        return@withContext result
    }
}