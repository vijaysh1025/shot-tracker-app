package com.shottracker.feature.home.models.pbp

import com.shottracker.feature.home.models.PlayersItem
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PlayByPlay(
	@Json(name="away")
	val away: Away? = null,

	@Json(name="home")
	val home: Home? = null,

	@Json(name="periods")
	val periods: List<PeriodsItem?>? = null,

	@Json(name="id")
	val id: String? = null
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
data class EventsItem(

	@Json(name="event_type")
	val eventType: String? = null,

	@Json(name="description")
	val description: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="attribution")
	val attribution: Attribution? = null,

	@Json(name="location")
	val location: Location? = null,

	@Json(name="statistics")
	val statistics: List<StatisticsItem?>? = null
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