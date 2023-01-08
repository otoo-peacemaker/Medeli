package com.peacecodetech.medeli.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentSignUpBinding
import com.peacecodetech.medeli.util.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : BaseFragment() {
    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()
    private val lViewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var isNetworkAvailable: NetworkManager

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

        binding.signInWithGoogle.setOnClickListener {
            signInWithGoogle()
        }

    }


    private fun registerUserEmailAndPassword() {
        val username = binding.nameId.text.toString()
        val emailText = binding.emailId.text?.trim().toString()
        val password = binding.passwordId.text.toString()
        val phoneNumber = binding.phoneId.text.toString()
        val confirmPassword = binding.passwordId.text.toString()

        if (password == confirmPassword) {
            viewModel.registerUser(
                email = emailText,
                password = password,
                phoneNumber = phoneNumber
            ).observe(viewLifecycleOwner) {
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
        } else {
            showDialog("Registration error", "Password and confirm password  mismatch") {}
        }
    }


    private fun signInWithGoogle() {
        if (isNetworkAvailable.isConnected()) {
            val signInIntent: Intent = googleSignInClient.signInIntent
            startForResult.launch(signInIntent)
        } else {
            view?.showSnackBar("Please, check your internet connection")
        }
    }

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Constants.RC_SIGN_IN) {
                val intent = result.data
                val task = GoogleSignIn.getSignedInAccountFromIntent(intent)
                try {
                    val account = task.getResult(ApiException::class.java)
                    lViewModel.signInWithGoogle(account!!).observe(viewLifecycleOwner) {
                        when (it?.status) {
                            Status.SUCCESS -> {
                                binding.signUpBtn.isEnabled = true
                                binding.signUpBtn.text = getString(R.string.sign_up)
                                // binding.normalLoader.visibility = View.INVISIBLE
                                if (findNavController().currentDestination?.id == R.id.signUpFragment) {
                                    context?.startHomeActivity()
                                    Timber.d("display ${it.data?.fullName} ")
                                }
                                viewModel.saveUser(
                                    fullName = it.data?.fullName!!,
                                    email = it.data.email!!,
                                    password = "null"
                                )
                            }
                            Status.ERROR -> {
                                requireView().showSnackBar(it.message!!)
                                binding.signUpBtn.isEnabled = true
                                binding.signUpBtn.text = getString(R.string.sign_up)
                            }
                            Status.LOADING -> {
                                binding.signUpBtn.isEnabled = false
                                binding.signUpBtn.text = getString(R.string.loading)
                            }
                            else -> {
                                //TODO
                            }
                        }
                    }
                } catch (e: ApiException) {
                    Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                }
            }
        }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}