package com.shottracker.feature.home.models.pbp

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


enum class EventType {
    twopointmade,
    twopointmiss,
    threepointmade,
    threepointmiss,
    freethrowmade,
    freethrowmiss,
    rebound,
    turnover,
    opentip,
    stoppage,
    personalfoul,
    shootingfoul,
    lineupchange,
    teamtimeout
}
