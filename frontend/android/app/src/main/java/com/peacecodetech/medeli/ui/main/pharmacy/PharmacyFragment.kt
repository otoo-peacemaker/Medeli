package com.peacecodetech.medeli.ui.main.pharmacy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peacecodetech.medeli.databinding.FragmentPharmacyBinding
import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.getJsonDataFromAsset
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class PharmacyFragment : BaseFragment() {

    private var _binding: FragmentPharmacyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager
    private val viewModel: PharmacyViewModel by viewModels()

    @Inject
    lateinit var firebaseDb: FirebaseDatabase
    lateinit var metadataRef: DatabaseReference

    private val arrListPharmacy = mutableListOf<Pharmacy>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // viewModel = ViewModelProvider(this)[PharmacyViewModel::class.java]
        _binding = FragmentPharmacyBinding.inflate(inflater, container, false)
       // viewPager = binding.viewPager
        metadataRef = firebaseDb.getReference("pharmacy-metadata")//reference to our data

//        metadataRef.removeValue()

        setupViewPager()
        setupTabLayout()

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


    private fun populatePharmacy() {
        metadataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jsonFileString =
                    getJsonDataFromAsset(requireContext(), "pharmacy_metadata.json")
                if (jsonFileString != null) {
                    val gson = Gson()
                    val objPharmacyType = object : TypeToken<List<Pharmacy>>() {}.type
                    val pharmacy: List<Pharmacy> = gson.fromJson(jsonFileString, objPharmacyType)
                    pharmacy.forEachIndexed { idx, data ->

                        metadataRef.child(idx.toString()).setValue(data)
                            .addOnCompleteListener {
                                //TODO
                            }.addOnFailureListener {
                                //TODO
                            }
                    }
                }
                // displaying a toast message.
                Toast.makeText(requireContext(), "Pharmacy Added..", Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {
                // displaying a failure message on below line.
                Toast.makeText(requireContext(), "Fail to add Pharmacy..", Toast.LENGTH_SHORT)
                    .show()
            }
        })

    }

    private fun getPharmacy() {
        // on below line clearing our list.
        arrListPharmacy.clear()
        metadataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val phaInfo = data.getValue(Pharmacy::class.java)
                    if (phaInfo != null) {
                        viewModel.insertIntoRoomPharmacyData(phaInfo)
                        arrListPharmacy.add(phaInfo)
                    }
                    Log.i("META", "{$phaInfo}")
                }
//                pharmacyAdapter.submitList(arrListPharmacy)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}