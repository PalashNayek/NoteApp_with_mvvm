package com.example.noteapp_with_mvvm.view_model

import android.text.TextUtils
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp_with_mvvm.model.signInRequest.SignInRequest
import com.example.noteapp_with_mvvm.model.signInResponse.SignInResponse
import com.example.noteapp_with_mvvm.model.signUpRequest.SignUpUserRequest
import com.example.noteapp_with_mvvm.model.signUpResponse.SignUpUserResponse
import com.example.noteapp_with_mvvm.repository.UserRepository
import com.example.noteapp_with_mvvm.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor (private val userRepository: UserRepository) : ViewModel() {

    val signUpUserResponseStateFlow : StateFlow<NetworkResult<SignUpUserResponse>>
    get() = userRepository.signUpResponseStateFlow

    val signInUserResponseStateFlow : StateFlow<NetworkResult<SignInResponse>>
        get() = userRepository.signInResponseStateFlow

    fun registerUserVM(signUpUserRequest: SignUpUserRequest){
        viewModelScope.launch {
            userRepository.registerUser(signUpUserRequest)
        }
    }

    fun loginUserVM(signInRequest: SignInRequest){
        viewModelScope.launch {
            userRepository.loginUser(signInRequest)
        }
    }

    fun validateRegisterCredentials(userName : String, emailAddress: String, password: String) : Pair<Boolean, String>{
        var result = Pair(true, "")

        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please fill-up all field")
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){

            result = Pair(false, "Please provide valid email")
        }else if (password.length<=5){
            result = Pair(false, "Password length should be greater then 5")
        }

        return result

    }
    fun validateLoginCredentials(emailAddress: String, password: String ) : Pair<Boolean, String>{
        var result = Pair(true, "")

        if (TextUtils.isEmpty(emailAddress) || TextUtils.isEmpty(password)){
            result = Pair(false, "Please fill-up all field")
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){

            result = Pair(false, "Please provide valid email")
        }else if (password.length<=5){
            result = Pair(false, "Password length should be greater then 5")
        }
        return result
    }
}