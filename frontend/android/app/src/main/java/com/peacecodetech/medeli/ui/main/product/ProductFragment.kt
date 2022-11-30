package com.peacecodetech.medeli.ui.main.product

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.FragmentHomeBinding
import com.peacecodetech.medeli.databinding.FragmentProductBinding

class ProductFragment : Fragment() {

    companion object {
        fun newInstance() = ProductFragment()
    }

    private lateinit var viewModel: ProductViewModel
    private var _binding: FragmentProductBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductBinding.inflate(inflater, container, false)
        productType()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
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

}