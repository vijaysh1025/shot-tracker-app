package com.shottracker.feature.home.utils.network

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor(private val apiKey:String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request().url.newBuilder()
            .addQueryParameter(API_KEY_PARAM, apiKey)
            .build()

        val request = chain.request().newBuilder().url(url).build()
        return chain.proceed(request)
    }

    companion object{
        private val API_KEY_PARAM = "api_key"
    }

}