package com.peacecodetech.medeli.ui.main.pharmacy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentSavedBinding
import com.peacecodetech.medeli.databinding.SavedListBinding
import com.peacecodetech.medeli.data.responses.Pharmacy
import com.peacecodetech.medeli.util.BaseFragment
import com.peacecodetech.medeli.util.getJsonDataFromAsset
import timber.log.Timber

class SavedFragment : BaseFragment(), RecyclerAdapter.OnViewDetail,
    RecyclerAdapter.OnSelectedItemListener {

    companion object {
        fun newInstance() = SavedFragment()
    }

    private lateinit var viewModel: SavedViewModel
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!


    private val pharmacyAdapter: RecyclerAdapter by lazy {
        RecyclerAdapter(this, this) { _, data ->
            //get item  on selected row
            Timber.d("YOU CLICK  FRAGMENT \n $data")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)

        binding.materialButton.setOnClickListener {
            routUserToMap("4.908538200000001,-1.7563672", "4.989325299999999,-1.7562893")
        }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[SavedViewModel::class.java]
        // TODO: Use the ViewModel

        viewModelObservers()
        initRecView()

    }

    private fun viewModelObservers() {
        /* lifecycleScope.launch {
             viewModel.getRoomPharmacyData.observe(viewLifecycleOwner, Observer { list ->
                 list?.let {
                     // pharmacyAdapter.submitList(it)
                 }
             })
         }*/
    }

    private fun initRecView() {
        val recyclerView = binding.recyclerView
        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = pharmacyAdapter
        }

        loadJsonFile()
    }


    override fun onSelectedItemListener(viewListBinding: SavedListBinding) {
        viewListBinding.apply {
            call.button.apply {
                text = context.getString(R.string.call)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_call)
                setOnClickListener {
                    Timber.tag("TAG").d("Not yet implemented")
                }
            }
            view.button.apply {
                text = context.getString(R.string.view)
                setOnClickListener {
                    Timber.tag("TAG").d("Not yet implemented")
                }
            }
            chat.button.apply {
                text = context.getString(R.string.chat)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_chat)
                setOnClickListener {
                   // findNavController().navigate(R.id.action_savedFragment_to_navigation_chat)
                }
            }
            favorite.button.apply {
                text = context.getString(R.string.fav)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_outline_shopping_cart_24)
                setOnClickListener {
                   // findNavController().navigate(R.id.action_savedFragment_to_navigation_search)
                }
            }
            share.button.apply {
                text = context.getString(R.string.share)
                icon = AppCompatResources.getDrawable(context, R.drawable.ic_share)
                setOnClickListener {
                    sharedSheet(viewListBinding.pharmacyName.text.toString(),viewListBinding.description.text.toString())
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
            pharmacy.forEach { data ->
                data.let { product.add(it) }
            }
            pharmacyAdapter.submitList(product)
        }
    }

    override fun onOnViewDetail(pharmacy: Pharmacy) {
        Timber.tag("TAG").d("Not yet implemented")
    }


}