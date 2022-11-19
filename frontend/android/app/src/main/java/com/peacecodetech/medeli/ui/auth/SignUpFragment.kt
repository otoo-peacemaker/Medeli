package com.peacecodetech.medeli.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentSignUpBinding
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.Status
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signInText.setOnClickListener {
            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }

        binding.signUpBtn.setOnClickListener {
            registerUserEmailAndPassword()
        }

    }


    private fun registerUserEmailAndPassword() {
        val username = binding.nameId.text.toString()
        val emailText = binding.emailId.text?.trim().toString()
        val password = binding.passwordId.text.toString()
        val phoneNumber = binding.phoneId.text.toString()
        val confirmPassword = binding.passwordId.text.toString()

        if (password == confirmPassword){
            viewModel.registerUser(email = emailText, password = password, phoneNumber = phoneNumber).observe(viewLifecycleOwner) {
                when (it.status) {
                    Status.LOADING -> {
                        //TODO
                        showProgressBar()
                    }
                    Status.SUCCESS -> {
                        viewModel.saveUser(username, emailText, password)//Save data to the room
                        Timber.tag("Login user").d("::::::::::${it.data}")
                        showDialog(
                            "Sign up successfully",
                            "Please, verify your account from $emailText"
                        ) {
                            findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                        }
                        //TODO
                    }
                    Status.ERROR -> {
                        showDialog("Registration error", it.message!!) {}
                    }
                }
            }
        }else {
            showDialog("Registration error","Password and confirm password  mismatch") {}
        }

    }


    private fun signUpWithGoogle() {
        val webClientID = ""
        //TODO
      /*  viewModel.signUpWithGoogle(webClientID).observe(viewLifecycleOwner) {

        }*/
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}