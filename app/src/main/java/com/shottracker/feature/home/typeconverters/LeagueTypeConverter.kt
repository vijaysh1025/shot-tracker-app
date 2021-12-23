package com.shottracker.feature.home.typeconverters

import androidx.room.TypeConverter
import com.shottracker.feature.home.models.League
import com.shottracker.feature.home.models.LeagueJsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class LeagueTypeConverter {
    @TypeConverter
    fun toJson(league: League?): String? {
        Moshi.Builder().add(KotlinJsonAdapterFactory())
        if (league == null)
            return null
        return LeagueJsonAdapter(Moshi.Builder().build()).toJson(league)
    }

    @TypeConverter
    fun fromJson(data: String?): League? {
        if (data == null)
            return null
        return LeagueJsonAdapter(Moshi.Builder().build()).fromJson(data)
    }
}