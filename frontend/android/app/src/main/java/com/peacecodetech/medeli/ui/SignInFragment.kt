package com.peacecodetech.medeli.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.common.api.ApiException
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentSignInBinding
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.Status
import com.peacecodetech.medeli.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()

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


        binding.signUpText.setOnClickListener {
            findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
        }


    }


    private fun loginUser() {
        with(binding) {
            val email = emailId.text.toString()
            val password = passwordId.text.toString()

            try {
                viewModel.loginUser(email, password).observe(viewLifecycleOwner) {
                    when (it?.status) {
                        Status.SUCCESS -> {
                            Timber.tag("Login user").d("::::::::::${it.data}")
                            showDialog(" Login", "Login successful") {
                                //TODO
                            }
                        }
                        Status.ERROR -> {
                            showDialog("Login Error", "${it.message}") {
                             //   findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                            }
                        }
                        Status.LOADING -> {
                            //TODO()
                        }
                        null -> {
                           //TODO
                        }
                    }
                }
            } catch (e: ApiException) {
                showDialog("Login ApiException", "${e.message}") {
                    //  findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)
                }
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}