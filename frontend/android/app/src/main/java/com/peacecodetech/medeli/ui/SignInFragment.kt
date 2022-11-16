package com.peacecodetech.medeli.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentSignInBinding
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.viewmodel.SignInViewModel

class SignInFragment : BaseFragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SignInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.loginBtn.setOnClickListener {
            loginUser()
        }

        viewModel.firebaseUserLiveDataObserver.observe(requireActivity()) {
            if (it==null){
                showDialog("Login Error","No credentials found")
            }else{
                //TODO: navigate the user to home page
            }
        }


        binding.signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }


    }


    private fun loginUser(){
        with(binding){
            val email = emailId.text.toString()
            val password  = passwordId.text.toString()
            viewModel.loginUser(email,password)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}