package com.shottracker.feature.home.models

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.shottracker.feature.home.typeconverters.GamesItemListTypeConverter
import com.shottracker.feature.home.typeconverters.LeagueTypeConverter
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "dailyschedule")
data class DailySchedule(

	@NonNull
	@Json(name="date")
	@PrimaryKey
	@ColumnInfo(name = "date")
	val date: String = "id",

	@Json(name="league")
	@TypeConverters(LeagueTypeConverter::class)
	@ColumnInfo(name = "league")
	val league: League? = null,

	@Json(name="games")
	@TypeConverters(GamesItemListTypeConverter::class)
	@ColumnInfo(name = "games")
	val games: List<GamesItem?>? = null
)

@JsonClass(generateAdapter = true)
data class GamesItem(

	@Json(name="coverage")
	val coverage: String? = null,

	@Json(name="time_zones")
	val timeZones: TimeZones? = null,

	@Json(name="venue")
	val venue: Venue? = null,

	@Json(name="away")
	val away: Away? = null,

	@Json(name="scheduled")
	val scheduled: String? = null,

	@Json(name="broadcasts")
	val broadcasts: List<BroadcastsItem?>? = null,

	@Json(name="home")
	val home: Home? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="home_points")
	val homePoints: Int? = null,

	@Json(name="away_points")
	val awayPoints: Int? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="track_on_court")
	val trackOnCourt: Boolean? = null,

	@Json(name="status")
	val status: String? = null
)

@JsonClass(generateAdapter = true)
data class BroadcastsItem(

	@Json(name="channel")
	val channel: String? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="locale")
	val locale: String? = null,

	@Json(name="network")
	val network: String? = null
)
