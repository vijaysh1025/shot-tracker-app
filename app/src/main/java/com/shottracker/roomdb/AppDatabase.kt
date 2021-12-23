package com.shottracker.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shottracker.feature.home.data.local.DailyScheduleDao
import com.shottracker.feature.home.models.DailySchedule
import com.shottracker.feature.home.typeconverters.GamesItemListTypeConverter
import com.shottracker.feature.home.typeconverters.LeagueTypeConverter

/**
 * App database component that builds the room database that can be injected anywhere in the app (Since the module is scoped to [@CoreScope])
 */

@TypeConverters(
    GamesItemListTypeConverter::class,
    LeagueTypeConverter::class,
)
@Database(entities = [DailySchedule::class], version = 3, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dailyScheduleDao(): DailyScheduleDao
}