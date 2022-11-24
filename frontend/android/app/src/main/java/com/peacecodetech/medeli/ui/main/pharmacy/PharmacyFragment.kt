package com.peacecodetech.medeli.ui.main.pharmacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.peacecodetech.medeli.databinding.FragmentPharmacyBinding

class PharmacyFragment : Fragment() {

    private var _binding: FragmentPharmacyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[PharmacyViewModel::class.java]

        _binding = FragmentPharmacyBinding.inflate(inflater, container, false)

        viewPager = binding.viewPager
        val adapter = PagerAdapter(requireActivity())
        viewPager.adapter = adapter

       // binding.tabL.setupWithViewPager(viewPager)

        binding.tabL.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {

            override fun onTabSelected(tab: TabLayout.Tab?) {
                // Handle tab select
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Handle tab reselect
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Handle tab unselect
            }
        })

        return  binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}