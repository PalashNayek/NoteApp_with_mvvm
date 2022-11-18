package com.example.noteapp_with_mvvm.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.noteapp_with_mvvm.R
import com.example.noteapp_with_mvvm.databinding.FragmentRegisterBinding
import com.example.noteapp_with_mvvm.model.signUpRequest.SignUpUserRequest
import com.example.noteapp_with_mvvm.utils.NetworkResult
import com.example.noteapp_with_mvvm.utils.TokenManager
import com.example.noteapp_with_mvvm.view_model.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val authViewModel by viewModels<AuthViewModel>()

    @Inject
    lateinit var tokenManager : TokenManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        if (tokenManager.getToken() != null){
            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Already have an account click..............................
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        //SignUp button click.................................
        binding.btnSignUp.setOnClickListener {

            val validationResult = validateUserInput()
            if (validationResult.first) {
                authViewModel.registerUserVM(getUserRequest())
            } else {
                binding.txtError.text = validationResult.second
            }
        }

        //fetching signup response.........................................................................
        bindObserver()
    }

    private fun getUserRequest(): SignUpUserRequest {
        val emailAddress = binding.txtEmail.text.toString()
        val password = binding.txtPassword.text.toString()
        val userName = binding.txtUsername.text.toString()

        return SignUpUserRequest(emailAddress, password, userName)
    }

    private fun validateUserInput(): Pair<Boolean, String> {
        val userRequest = getUserRequest()
        return authViewModel.validateRegisterCredentials(
            userRequest.username,
            userRequest.email,
            userRequest.password
        )
    }

    private fun bindObserver() {
        /*authViewModel.signUpUserResponseLiveData.observe(viewLifecycleOwner, Observer {

        })*/
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                authViewModel.signUpUserResponseStateFlow.collect{
                    binding.progressBar.isVisible = false
                    when (it) {
                        is NetworkResult.Success -> {
                            //save Token..............
                            tokenManager.saveToken(it.data!!.token)
                            findNavController().navigate(R.id.action_registerFragment_to_mainFragment)
                        }

                        is NetworkResult.Error -> {
                            binding.txtError.text = it.message
                        }

                        is NetworkResult.Loading -> {
                            binding.progressBar.isVisible = true
                        }
                        else -> {
                            Toast.makeText(context, "Something app error", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}