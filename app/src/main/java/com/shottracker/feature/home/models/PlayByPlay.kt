package com.shottracker.feature.home.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class PlayByPlay(

	@Json(name="coverage")
	val coverage: String? = null,

	@Json(name="time_zones")
	val timeZones: TimeZones? = null,

	@Json(name="away")
	val away: Away? = null,

	@Json(name="clock_decimal")
	val clockDecimal: String? = null,

	@Json(name="scheduled")
	val scheduled: String? = null,

	@Json(name="entry_mode")
	val entryMode: String? = null,

	@Json(name="deleted_events")
	val deletedEvents: List<DeletedEventsItem?>? = null,

	@Json(name="clock")
	val clock: String? = null,

	@Json(name="home")
	val home: Home? = null,

	@Json(name="duration")
	val duration: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="times_tied")
	val timesTied: Int? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="lead_changes")
	val leadChanges: Int? = null,

	@Json(name="periods")
	val periods: List<PeriodsItem?>? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="track_on_court")
	val trackOnCourt: Boolean? = null,

	@Json(name="attendance")
	val attendance: Int? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="quarter")
	val quarter: Int? = null
)

@JsonClass(generateAdapter = true)
data class Home(

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="bonus")
	val bonus: Boolean? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="points")
	val points: Int? = null,

	@Json(name="players")
	val players: List<PlayersItem?>? = null
)

@JsonClass(generateAdapter = true)
data class Attribution(

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="team_basket")
	val teamBasket: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class TimeZones(

	@Json(name="venue")
	val venue: String? = null,

	@Json(name="away")
	val away: String? = null,

	@Json(name="home")
	val home: String? = null
)

@JsonClass(generateAdapter = true)
data class EventsItem(

	@Json(name="sequence")
	val sequence: Long? = null,

	@Json(name="number")
	val number: Int? = null,

	@Json(name="event_type")
	val eventType: String? = null,

	@Json(name="clock_decimal")
	val clockDecimal: String? = null,

	@Json(name="home_points")
	val homePoints: Int? = null,

	@Json(name="away_points")
	val awayPoints: Int? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="wall_clock")
	val wallClock: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="clock")
	val clock: String? = null,

	@Json(name="updated")
	val updated: String? = null,

	@Json(name="possession")
	val possession: Possession? = null,

	@Json(name="qualifiers")
	val qualifiers: List<QualifiersItem?>? = null,

	@Json(name="attribution")
	val attribution: Attribution? = null,

	@Json(name="on_court")
	val onCourt: OnCourt? = null,

	@Json(name="location")
	val location: Location? = null,

	@Json(name="statistics")
	val statistics: List<StatisticsItem?>? = null,

	@Json(name="turnover_type")
	val turnoverType: String? = null,

	@Json(name="attempt")
	val attempt: String? = null,

	@Json(name="duration")
	val duration: Int? = null
)

data class OnCourt(

	@Json(name="away")
	val away: Away? = null,

	@Json(name="home")
	val home: Home? = null
)

data class DeletedEventsItem(

	@Json(name="id")
	val id: String? = null
)

data class QualifiersItem(

	@Json(name="qualifier")
	val qualifier: String? = null
)

@JsonClass(generateAdapter = true)
data class Team(

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class StatisticsItem(

	@Json(name="shot_type")
	val shotType: String? = null,

	@Json(name="shot_type_desc")
	val shotTypeDesc: String? = null,

	@Json(name="shot_distance")
	val shotDistance: Double? = null,

	@Json(name="made")
	val made: Boolean? = null,

	@Json(name="team")
	val team: Team? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="player")
	val player: Player? = null,

	@Json(name="rebound_type")
	val reboundType: String? = null,

	@Json(name="points")
	val points: Int? = null,

	@Json(name="three_point_shot")
	val threePointShot: Boolean? = null,

	@Json(name="free_throw_type")
	val freeThrowType: String? = null
)

@JsonClass(generateAdapter = true)
data class Player(

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="full_name")
	val fullName: String? = null,

	@Json(name="jersey_number")
	val jerseyNumber: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class PeriodsItem(

	@Json(name="number")
	val number: Int? = null,

	@Json(name="sequence")
	val sequence: Int? = null,

	@Json(name="scoring")
	val scoring: Scoring? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="type")
	val type: String? = null,

	@Json(name="events")
	val events: List<EventsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class Scoring(

	@Json(name="times_tied")
	val timesTied: Int? = null,

	@Json(name="away")
	val away: Away? = null,

	@Json(name="lead_changes")
	val leadChanges: Int? = null,

	@Json(name="home")
	val home: Home? = null
)

@JsonClass(generateAdapter = true)
data class Location(

	@Json(name="coord_x")
	val coordX: Int? = null,

	@Json(name="coord_y")
	val coordY: Int? = null,

	@Json(name="action_area")
	val actionArea: String? = null
)

@JsonClass(generateAdapter = true)
data class Away(

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="bonus")
	val bonus: Boolean? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="points")
	val points: Int? = null,

	@Json(name="players")
	val players: List<PlayersItem?>? = null
)

@JsonClass(generateAdapter = true)
data class Possession(

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: String? = null
)
