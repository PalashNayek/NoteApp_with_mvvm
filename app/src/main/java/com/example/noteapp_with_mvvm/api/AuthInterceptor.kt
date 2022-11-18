package com.example.noteapp_with_mvvm.api

import android.util.Log
import com.example.noteapp_with_mvvm.utils.TokenManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor() : Interceptor {
    @Inject
    lateinit var tokenManager: TokenManager
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
        Log.d("MyRequest", request.build().toString())
        val token = tokenManager.getToken()
        request.addHeader("Authorization", "Bearer $token")
        return chain.proceed(request.build())
    }
}