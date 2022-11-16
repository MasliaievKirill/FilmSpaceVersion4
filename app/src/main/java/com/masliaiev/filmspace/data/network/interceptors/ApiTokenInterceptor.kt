package com.masliaiev.filmspace.data.network.interceptors

import com.masliaiev.filmspace.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class ApiTokenInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val requestUrl = originalRequest.url().newBuilder()
            .addQueryParameter(QUERY_PARAM_API_KEY, BuildConfig.API_KEY)
            .build()

        val request = originalRequest.newBuilder().url(requestUrl).build()

        return chain.proceed(request)
    }

    companion object {
        private const val QUERY_PARAM_API_KEY = "api_key"
    }
}