package com.wapo.flagship.network.retrofit.network

import com.shottracker.network.Result
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * [Result]s wrapper. Converts raw [Response]s into the [Result]s.
 */
class APIResultCall<T>(
    proxy: Call<T>
) : CallDelegate<T, Result<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<Result<T>>) = proxy.enqueue(object :
        Callback<T> {

        override fun onResponse(call: Call<T>, response: Response<T>) {
            val code = response.code()
            val result = if (code in 200 until 300) {
                val body = response.body()
                Result.Success<T>(body)
            } else {
                Result.Failure(code, response.errorBody()?.string())
            }

            callback.onResponse(
                this@APIResultCall,
                Response.success(result)
            )
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            val result = if (t is IOException) {
                Result.NetworkError(t)
            } else {
                Result.Failure(UNKNOWN, t.message)
            }

            callback.onResponse(
                this@APIResultCall,
                Response.success(result)
            )
        }

    })

    override fun cloneImpl() = APIResultCall(proxy.clone())

    companion object {

        const val UNKNOWN = -1

    }

}