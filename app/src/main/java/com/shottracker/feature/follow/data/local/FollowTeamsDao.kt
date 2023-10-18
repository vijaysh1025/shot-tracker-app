package com.shottracker.feature.home.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shottracker.feature.home.models.DailySchedule

@Dao
interface DailyScheduleDao {

    @Query("SELECT * FROM dailyschedule where date = :date")
    suspend fun getGamesForDate(date:String) : DailySchedule

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveGames(dailySchedule: DailySchedule)

}