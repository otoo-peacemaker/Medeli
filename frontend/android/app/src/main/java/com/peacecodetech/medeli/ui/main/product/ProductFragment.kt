package com.peacecodetech.medeli.ui.main.product

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.peacecodetech.medeli.databinding.FragmentProductBinding
import com.peacecodetech.medeli.databinding.ProductListBinding
import com.peacecodetech.medeli.model.Categories
import com.peacecodetech.medeli.model.Products
import com.peacecodetech.medeli.util.getJsonDataFromAsset
import timber.log.Timber

class ProductFragment : Fragment(), ProductAdapter.OnSelectedItemListener,
    ProductAdapter.OnViewDetail {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private lateinit var viewModel: ProductViewModel
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(this, this) { _, data ->
            //get item  on selected row
            Timber.d("YOU CLICK  FRAGMENT \n $data")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        productType()
        initRecView()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
    }

    private fun initRecView() {
        val recyclerView = binding.recyclerView
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
            adapter = productAdapter
        }
        loadJsonFile()
    }

    private fun loadJsonFile() {
        val jsonFileString = getJsonDataFromAsset(requireContext(), "products.json")
        val product = mutableListOf<Products>()
        if (jsonFileString != null) {
            val gson = Gson()
            val objPharmacyType = object : TypeToken<List<Products>>() {}.type
            val pharmacy: List<Products> = gson.fromJson(jsonFileString, objPharmacyType)
            pharmacy.forEach { data ->
                data.let { product.add(it) }
            }
            productAdapter.submitList(product)

        }
    }

    private fun productType() = arguments?.let {
        val details = it.getString("name")
        val color = it.getInt("color")
        binding.selectedProduct.text = details.toString()
        binding.selectedProduct.setBackgroundColor(color)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSelectedItemListener(viewListBinding: ProductListBinding) {
        Timber.tag("TAG").d("Not yet implemented")
    }

    override fun onOnViewDetail(pharmacy: Products) {
        Timber.tag("TAG").d("Not yet implemented")
    }

}