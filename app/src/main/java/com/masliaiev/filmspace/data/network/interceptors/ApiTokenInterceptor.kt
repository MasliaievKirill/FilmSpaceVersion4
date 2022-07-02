package com.masliaiev.filmspace.data.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class ApiTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestUrl = originalRequest.url().newBuilder()
            .addQueryParameter(QUERY_PARAM_API_KEY, API_KEY)
            .build()

        val request = originalRequest.newBuilder().url(requestUrl).build()

        return chain.proceed(request)
    }

    companion object{
        private const val QUERY_PARAM_API_KEY = "api_key"

        private const val API_KEY = "9f9d136877ade7608f32a571c18756be"
    }
}