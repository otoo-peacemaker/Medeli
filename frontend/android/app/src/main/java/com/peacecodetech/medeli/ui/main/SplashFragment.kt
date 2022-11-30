package com.peacecodetech.medeli.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.ActivityMainBinding
import com.peacecodetech.medeli.databinding.FragmentSplashBinding
import com.peacecodetech.medeli.util.BaseFragment

class SplashFragment : BaseFragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        setProgressBar((requireActivity().findViewById(R.id.progressId)))
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginBtn.setOnClickListener {
            showProgressBar()
            findNavController().navigate(R.id.action_splashFragment2_to_signInFragment)
        }

        binding.signUpBtn.setOnClickListener {
            showProgressBar()
            findNavController().navigate(R.id.action_splashFragment2_to_signUpFragment) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
