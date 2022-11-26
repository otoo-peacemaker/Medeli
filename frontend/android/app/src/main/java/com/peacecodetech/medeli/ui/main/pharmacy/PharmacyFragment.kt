package com.peacecodetech.medeli.ui.main.pharmacy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentPharmacyBinding
import com.peacecodetech.medeli.databinding.SavedListBinding
import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.util.Status
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class PharmacyFragment : Fragment(), RecyclerAdapter.OnViewDetail,
    RecyclerAdapter.OnSelectedItemListener {

    private var _binding: FragmentPharmacyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager
    private val viewModel:PharmacyViewModel by viewModels()
    private val pharmacyAdapter: RecyclerAdapter by lazy {
        RecyclerAdapter(this, this) { _, data ->
            //get item  on selected row
            Timber.d("YOU CLICK  FRAGMENT \n $data")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       // viewModel = ViewModelProvider(this)[PharmacyViewModel::class.java]
        _binding = FragmentPharmacyBinding.inflate(inflater, container, false)
        viewPager = binding.viewPager
        /*//val adapter = PagerAdapter(requireActivity())
        //viewPager.adapter = adapter
*/

        setupViewPager()
        setupTabLayout()
        initRecView()
        viewModelObservers()

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
        val recyclerView = binding.savedFragment.recyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pharmacyAdapter
        }
    }



    fun viewModelObservers(){
        lifecycleScope.launch {
            viewModel.pharmacyData
                .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { data ->
                    if (data != null) {
                        when(data.status){
                            Status.SUCCESS -> {
                                Log.d("String","$data")
                                pharmacyAdapter.submitList(data.data)
                            }
                            Status.ERROR -> {
                                TODO()
                            }
                            Status.LOADING -> {
                                TODO()
                            }
                        }

                    }
                }
        }
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}