package com.peacecodetech.medeli.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentPasswordResetBinding
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.Status
import com.peacecodetech.medeli.viewmodel.SignInViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordResetFragment : BaseFragment() {
    private var _binding: FragmentPasswordResetBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPasswordResetBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            sendEmailId.loginBtn.apply {
                text = getString(R.string.send_email)
                setOnClickListener {
                    viewModel.sendPasswordResetEmail(emailId.nameId.text.toString())
                        .observe(viewLifecycleOwner) {
                            when (it?.status) {
                                Status.SUCCESS -> {
                                    showDialog("Password reset sent", "Please check your ${emailId.nameId.text.toString()} to reset your password") {
                                        findNavController().navigate(R.id.action_passwordResetFragment_to_signInFragment)
                                    }
                                }
                                Status.ERROR -> {
                                    showDialog("Password reset error", "${it.message}") {
                                      //  findNavController().navigate(R.id.action_passwordResetFragment_to_signInFragment)
                                    }
                                }
                                Status.LOADING -> TODO()
                                null -> TODO()
                            }
                        }
                }
            }
        }
    }
}