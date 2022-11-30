package com.peacecodetech.medeli.ui.main.pharmacy

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentPharmacyBinding
import com.peacecodetech.medeli.databinding.SavedListBinding
import com.peacecodetech.medeli.model.Pharmacy
import com.peacecodetech.medeli.model.Products
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.getJsonDataFromAsset
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@AndroidEntryPoint
class PharmacyFragment : BaseFragment(), RecyclerAdapter.OnViewDetail,
    RecyclerAdapter.OnSelectedItemListener {

    private var _binding: FragmentPharmacyBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewPager: ViewPager
    private val viewModel: PharmacyViewModel by viewModels()

    @Inject
    lateinit var firebaseDb: FirebaseDatabase
    lateinit var metadataRef: DatabaseReference

    private val arrListPharmacy = mutableListOf<Pharmacy>()

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
        metadataRef = firebaseDb.getReference("pharmacy-metadata")//reference to our data

//        metadataRef.removeValue()
//        populatePharmacy()

        setupViewPager()
        setupTabLayout()
        initRecView()
        viewModelObservers()
        getPharmacy()

        binding.savedFragment.materialButton.setOnClickListener {
            routUserToMap("4.908538200000001,-1.7563672", "4.989325299999999,-1.7562893")
        }

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

      loadJsonFile()
    }

    private fun viewModelObservers() {
        lifecycleScope.launch {
            viewModel.getRoomPharmacyData.observe(viewLifecycleOwner, Observer { list ->
                list?.let {
                   // pharmacyAdapter.submitList(it)
                }
            })
        }
    }

    override fun onOnViewDetail(pharmacy: Pharmacy) {
        Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_LONG).show()
    }

    override fun onSelectedItemListener(viewListBinding: SavedListBinding) {

        viewListBinding.apply {
            call.button.apply {
                text = context.getString(R.string.call)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_call)
                setOnClickListener {
                    Toast.makeText(requireContext(), "TODO", Toast.LENGTH_LONG).show()
                }
            }
            view.button.apply {
                text = context.getString(R.string.view)
                setOnClickListener {
                    Toast.makeText(requireContext(), "TODO", Toast.LENGTH_LONG).show()
                }
            }
            chat.button.apply {
                text = context.getString(R.string.chat)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_chat)
                setOnClickListener {
                    Toast.makeText(requireContext(), "TODO", Toast.LENGTH_LONG).show()
                }
            }
            favorite.button.apply {
                text = context.getString(R.string.fav)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_favorite)
                setOnClickListener {
                    Toast.makeText(requireContext(), "TODO", Toast.LENGTH_LONG).show()
                }
            }
            share.button.apply {
                text = context.getString(R.string.share)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_share)
                setOnClickListener {
                    Toast.makeText(requireContext(), "TODO", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    private fun loadJsonFile() {
        val product = mutableListOf<Pharmacy>()
        val jsonFileString = getJsonDataFromAsset(requireContext(), "pharmacy_metadata.json")

        if (jsonFileString != null) {
            val gson = Gson()
            val objPharmacyType = object : TypeToken<List<Pharmacy>>() {}.type
            val pharmacy: List<Pharmacy> = gson.fromJson(jsonFileString, objPharmacyType)
            pharmacy.forEach {  data ->
                data.let { product.add(it) }
            }
            pharmacyAdapter.submitList(product)
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
                pharmacyAdapter.submitList(arrListPharmacy)
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