package com.shottracker.feature.follow.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.shottracker.feature.home.models.DailySchedule

@Dao
interface FollowTeamsDao {

    @Query("SELECT * FROM dailyschedule where date = :date")
    suspend fun getFollowedTeams(date:String) : DailySchedule

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFollowedTeams(list: List)

}