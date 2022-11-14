package com.peacecodetech.medeli

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.peacecodetech.medeli.data.repository.UserAuthenticationRepository
import com.peacecodetech.medeli.data.source.FirebaseDatastore
import com.peacecodetech.medeli.databinding.ActivityMainBinding
import com.peacecodetech.medeli.databinding.FragmentSplashBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){

        }

    }
}