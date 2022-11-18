package com.example.noteapp_with_mvvm.api

import com.example.noteapp_with_mvvm.model.signInRequest.SignInRequest
import com.example.noteapp_with_mvvm.model.signInResponse.SignInResponse
import com.example.noteapp_with_mvvm.model.signUpRequest.SignUpUserRequest
import com.example.noteapp_with_mvvm.model.signUpResponse.SignUpUserResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface MyApi {

    @POST("/users/signup")
    suspend fun signup(@Body signUpUserRequest: SignUpUserRequest) : Response<SignUpUserResponse>

    @POST("/users/signin")
    suspend fun signin(@Body signInRequest: SignInRequest) : Response<SignInResponse>
}