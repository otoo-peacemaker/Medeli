package com.peacecodetech.medeli.ui.main.pharmacy

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
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
                SavedFragment()
            }
            else -> {
                NearestFragment()
            }
        }
    }
}

class ViewPagerAdapter(fm: FragmentManager, private var tabCount: Int) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> SavedFragment()
            else ->  NearestFragment()
        }
    }

    override fun getCount(): Int {
        return tabCount
    }

    override fun getPageTitle(position: Int): CharSequence {
        return "Tab " + (position + 1)
    }
}



const val NUM_PAGES = 4