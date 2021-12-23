package com.wapo.flagship.network.retrofit.network

import com.shottracker.network.Result
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * CallAdapter Factory that wraps around network responses and always emits a Result or a Response,
 * depending on the request in the Retrofit Services even if a network request fails
 * or when one of the converters (e.g. Moshi) throws an exception.
 */
class CallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> {
            val callType = getParameterUpperBound(
                0,
                returnType as ParameterizedType
            )
            when (getRawType(callType)) {
                Result::class.java -> {
                    val resultType =
                        getParameterUpperBound(
                            0,
                            callType as ParameterizedType
                        )
                    ApiResultAdapter(
                        resultType
                    )
                }
                else -> null
            }

        }
        else -> null
    }

}