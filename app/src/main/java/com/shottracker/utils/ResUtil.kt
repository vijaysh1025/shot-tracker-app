package com.shottracker.utils

import android.content.Context
import androidx.annotation.DrawableRes
import androidx.compose.ui.text.toLowerCase
import com.example.shot_tracker_app.R
import com.squareup.moshi.Moshi
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

object ResUtil {
    fun <T : Any> fromRawJson(clazz: Class<T>, context: Context, id: Int): T? {
        context.resources.openRawResource(id).let {
            val moshi = Moshi.Builder().build()
            val jsonData = BufferedReader(InputStreamReader(it)).use { br ->
                br.readText()
            }
            return moshi.adapter(clazz).fromJson(jsonData)
        }
    }

    fun loadIcon(alias: String?, context: Context): Int {
        alias?.lowercase()?.let {
            return context.resources.getIdentifier(
                it,
                "drawable",
                context.applicationInfo.packageName
            )
        }
        return R.drawable.fallback_icon
    }
}
