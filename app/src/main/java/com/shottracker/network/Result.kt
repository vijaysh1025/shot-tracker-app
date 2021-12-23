package com.shottracker.network

import java.net.HttpURLConnection

/**
 * Base Result class that wraps API responses into successes or failures.
 */
sealed class Result<out T> {

    data class Success<T>(val data: T?) : Result<T>()
    data class Failure(val statusCode: Int, val rawResponse: String? = null) : Result<Nothing>() {

        companion object {
            const val DB_FAILED_TO_READ = -200

            /**
             * Helper function to group http error statuses that describe
             * a connection problem with the API
             */
            fun Int.isConnectionProblem(): Boolean {
                return when (this) {
                    HttpURLConnection.HTTP_FORBIDDEN, // When a url requires a VPN connection
                    HttpURLConnection.HTTP_CLIENT_TIMEOUT -> true
                    else -> false
                }
            }

        }

    }

    class NetworkError(val error: Throwable) : Result<Nothing>()
}