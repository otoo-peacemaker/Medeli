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
import com.peacecodetech.medeli.databinding.FragmentSignInBinding
import com.peacecodetech.medeli.util.*
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SignInFragment : BaseFragment() {

    private var _binding: FragmentSignInBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignInViewModel by viewModels()

    @Inject
    lateinit var googleSignInClient: GoogleSignInClient

    @Inject
    lateinit var isNetworkAvailable: NetworkManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListeners()
    }


    private fun setListeners() {
        with(binding) {
            loginBtn.setOnClickListener {
                loginUser()
            }
            signUpText.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_signUpFragment) }
            forgotPss.setOnClickListener { findNavController().navigate(R.id.action_signInFragment_to_passwordResetFragment) }
            signInWithGoogle.setOnClickListener { signInWithGoogle() }
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
                            showDialog(" Login", "Login successful ${it.data}") {
                                context?.startHomeActivity()
                            }
                        }
                        Status.ERROR -> {
                            showDialog("Login Error", "${it.message}") {
                                //TODO AFTER OK BUTTON IS PRESSED
                            }
                        }
                        Status.LOADING -> {
                            //TODO() SET LOADING SCREEN
                        }
                        null -> {
                            // TODO : This is always null
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
                    viewModel.signInWithGoogle(account!!).observe(viewLifecycleOwner) {
                        when (it?.status) {
                            Status.SUCCESS -> {
                                // binding.signUpBtn.isEnabled = true
                                // binding.normalLoader.visibility = View.INVISIBLE
                                if (findNavController().currentDestination?.id == R.id.signUpFragment) {
                                    context?.startHomeActivity()
                                    Timber.d("display ${it.data?.fullName} ")
                                }
                            }
                            Status.ERROR -> {
                                requireView().showSnackBar(it.message!!)
                            }
                            Status.LOADING -> {
                                // binding.signUpBtn.isEnabled = false
                                //  binding.normalLoader.visibility = View.VISIBLE
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


    companion object {
        val TAG: String = SignInFragment::class.java.simpleName
        fun signInFragmentInstance() = SignInFragment()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}