package com.example.noteapp_with_mvvm.repository

import com.example.noteapp_with_mvvm.api.MyApi
import com.example.noteapp_with_mvvm.model.signInRequest.SignInRequest
import com.example.noteapp_with_mvvm.model.signInResponse.SignInResponse
import com.example.noteapp_with_mvvm.model.signUpRequest.SignUpUserRequest
import com.example.noteapp_with_mvvm.model.signUpResponse.SignUpUserResponse
import com.example.noteapp_with_mvvm.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val myApi: MyApi) {

    private val _signUpResponse = MutableStateFlow<NetworkResult<SignUpUserResponse>>(NetworkResult.Loading())
    val signUpResponseStateFlow : StateFlow<NetworkResult<SignUpUserResponse>>
    get() = _signUpResponse

    private val _signInResponse = MutableStateFlow<NetworkResult<SignInResponse>>(NetworkResult.Loading())
    val signInResponseStateFlow : StateFlow<NetworkResult<SignInResponse>>
        get() = _signInResponse

    suspend fun registerUser(signUpUserRequest: SignUpUserRequest){

        val response = myApi.signup(signUpUserRequest)
        handleSignUpResponse(response)
    }

    suspend fun loginUser(signInRequest: SignInRequest){
        val response = myApi.signin(signInRequest)
        handleSignInResponse(response)
    }

    private suspend fun handleSignUpResponse(response: Response<SignUpUserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _signUpResponse.emit(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _signUpResponse.emit(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _signUpResponse.emit(NetworkResult.Error("Something went to wrong"))
        }
    }

    private suspend fun handleSignInResponse(response: Response<SignInResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _signInResponse.emit(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
            _signInResponse.emit(NetworkResult.Error(errorObj.getString("message")))
        } else {
            _signInResponse.emit(NetworkResult.Error("Something went to wrong"))
        }
    }
}