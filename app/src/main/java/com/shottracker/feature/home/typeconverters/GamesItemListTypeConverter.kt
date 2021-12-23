package com.shottracker.feature.home.typeconverters

import androidx.room.TypeConverter
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import com.shottracker.feature.home.models.GamesItem
import com.shottracker.feature.home.models.GamesItemJsonAdapter
import com.squareup.moshi.Moshi

class GamesItemListTypeConverter {
    @TypeConverter
    fun toJson(items: List<GamesItem>?): String? {
        if (items == null)
            return null

        val arr = JsonArray()
        items.forEach {
            arr.add(GamesItemJsonAdapter(Moshi.Builder().build()).toJson(it))
        }
        return arr.toString()
    }

    @TypeConverter
    fun fromJson(data: String?): List<GamesItem>? {
        if (data == null)
            return null
        val items = mutableListOf<GamesItem>()
        val jsonArray = JsonParser.parseString(data) as JsonArray
        jsonArray.forEach {
            val item = GamesItemJsonAdapter(Moshi.Builder().build()).fromJson(it.asString)
            if (item != null)
                items.add(item)
        }

        return items
    }
}