package com.shottracker.feature.home.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Hierarchy(

	@Json(name="league")
	val league: League? = null,

	@Json(name="conferences")
	val conferences: List<ConferencesItem?>? = null
)

@JsonClass(generateAdapter = true)
data class Venue(

	@Json(name="zip")
	val zip: String? = null,

	@Json(name="country")
	val country: String? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="address")
	val address: String? = null,

	@Json(name="city")
	val city: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="state")
	val state: String? = null,

	@Json(name="capacity")
	val capacity: Int? = null
)

@JsonClass(generateAdapter = true)
data class ConferencesItem(

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null,

	@Json(name="divisions")
	val divisions: List<DivisionsItem?>? = null
)

@JsonClass(generateAdapter = true)
data class League(

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class DivisionsItem(

	@Json(name="teams")
	val teams: List<TeamsItem?>? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null
)

@JsonClass(generateAdapter = true)
data class TeamsItem(

	@Json(name="market")
	val market: String? = null,

	@Json(name="reference")
	val reference: String? = null,

	@Json(name="venue")
	val venue: Venue? = null,

	@Json(name="sr_id")
	val srId: String? = null,

	@Json(name="name")
	val name: String? = null,

	@Json(name="alias")
	val alias: String? = null,

	@Json(name="id")
	val id: String? = null
)
