package com.peacecodetech.medeli.ui.main.pharmacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentPharmacyBinding
import com.peacecodetech.medeli.databinding.SavedListBinding
import com.peacecodetech.medeli.model.Pharmacy
import timber.log.Timber

class PharmacyFragment : Fragment(), RecyclerAdapter.OnViewDetail,
    RecyclerAdapter.OnSelectedItemListener {

    private var _binding: FragmentPharmacyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel = ViewModelProvider(this)[PharmacyViewModel::class.java]
        _binding = FragmentPharmacyBinding.inflate(inflater, container, false)
        viewPager = binding.viewPager
        /*//val adapter = PagerAdapter(requireActivity())
        //viewPager.adapter = adapter
*/

        setupViewPager()
        setupTabLayout()
        initRecView()

        return binding.root
    }

    private fun setupViewPager() {
        binding.viewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, binding.tabL.tabCount)
            addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(binding.tabL))
        }
    }

    private fun setupTabLayout() {
        binding.tabL.apply {

            // tabGravity = TabLayout.GRAVITY_FILL
            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.position?.let {
                        binding.viewPager.currentItem = it
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {
                }

                override fun onTabReselected(tab: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun initRecView() {
        val pharmacyAdapter: RecyclerAdapter by lazy {
            RecyclerAdapter(this, this) { _, data ->
                //get item  on selected row
                Timber.d("YOU CLICK  FRAGMENT \n $data")
            }
        }
        val recyclerView = binding.savedFragment.recyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pharmacyAdapter
        }

        //TODO: submit student data from viewmodel
        val stdList = Student
        pharmacyAdapter.submitList(stdList.getStudentList())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onOnViewDetail(pharmacy: Pharmacy) {
        TODO("Not yet implemented")
    }

    override fun onSelectedItemListener(viewListBinding: SavedListBinding) {

        viewListBinding.apply {
            call.button.apply {
                text = context.getString(R.string.call)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_call)
            }
            view.button.apply {
                text = context.getString(R.string.view)
            }
            chat.button.apply {
                text = context.getString(R.string.chat)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_chat)
            }
            favorite.button.apply {
                text = context.getString(R.string.fav)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_favorite)
            }
            share.button.apply {
                text = context.getString(R.string.share)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_share)
            }
        }

    }
}