package com.shottracker.feature.home.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TeamProfile(

	@Json(name="venue")
	val venue: Venue? = null,

	@Json(name="conference")
	val conference: Conference? = null,

	@Json(name="players")
	val players: List<PlayersItem?>? = null,

	@Json(name="league")
	val league: League? = null,

	@Json(name="founded")
	val founded: Int? = null,

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="division")
	val division: Division? = null,

	@Json(name="coaches")
	val coaches: List<CoachesItem?>? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class Conference(

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class CoachesItem(

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="full_name")
	val fullName: String? = null,

	@Json(name="last_name")
	val lastName: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="position")
	val position: String? = null,

	@Json(name="experience")
	val experience: String? = null,

	@Json(name="first_name")
	val firstName: String? = null
)

@JsonClass(generateAdapter = true)
data class Division(

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class Draft(

	@Json(name="round")
	val round: String? = null,

	@Json(name="year")
	val year: Int? = null,

	@Json(name="pick")
	val pick: String? = null,

	@Json(name="team_id")
	val teamId: String? = null
)

@JsonClass(generateAdapter = true)
data class PlayersItem(

	@Json(name="college")
	val college: String? = null,

	@Json(name="abbr_name")
	val abbrName: String? = null,

	@Json(name="birthdate")
	val birthdate: String? = null,

	@Json(name="last_name")
	val lastName: String? = null,

	@Json(name="weight")
	val weight: Int? = null,

	@Json(name="birth_place")
	val birthPlace: String? = null,

	@Json(name="experience")
	val experience: String? = null,

	@Json(name="high_school")
	val highSchool: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="full_name")
	val fullName: String? = null,

	@Json(name="rookie_year")
	val rookieYear: Int? = null,

	@Json(name="draft")
	val draft: Draft? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="position")
	val position: String? = null,

	@Json(name="primary_position")
	val primaryPosition: String? = null,

	@Json(name="jersey_number")
	val jerseyNumber: String? = null,

	@Json(name="first_name")
	val firstName: String? = null,

	@Json(name="updated")
	val updated: String? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="height")
	val height: Int? = null,

	@Json(name="injuries")
	val injuries: List<InjuriesItem?>? = null,

	@Json(name="name_suffix")
	val nameSuffix: String? = null
)

@JsonClass(generateAdapter = true)
data class InjuriesItem(

	@Json(name="comment")
	val comment: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="update_date")
	val updateDate: String? = null,

	@Json(name="desc")
	val desc: String? = null,

	@Json(name="status")
	val status: String? = null,

	@Json(name="start_date")
	val startDate: String? = null
)
