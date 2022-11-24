package com.peacecodetech.medeli.ui.main.pharmacy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter


class PagerAdapter(fm: FragmentActivity) : FragmentStateAdapter(fm) {
    private val fragmentList = ArrayList<Fragment>()
    private val fragmentTitleList = ArrayList<String>()

    fun addFragment(fragment: Fragment, title: String) {
        fragmentList.add(fragment)
        fragmentTitleList.add(title)
    }

    override fun getItemCount(): Int = NUM_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                PharmacyFragment()
            }
            else -> {
                NearestFragment()
            }
        }
    }
}

const val NUM_PAGES = 4