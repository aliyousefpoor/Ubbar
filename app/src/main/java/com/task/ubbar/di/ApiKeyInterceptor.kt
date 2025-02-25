package com.task.ubbar.di

import android.util.Base64
import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val username = "09822222222"
        val password = "Sana12345678"
        val credentials = "$username:$password"
        val encodedCredentials = Base64.encodeToString(credentials.toByteArray(), Base64.NO_WRAP)
        val request = chain.request().newBuilder()
            .addHeader("accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Basic $encodedCredentials")
            .build()
        return chain.proceed(request)
    }
}