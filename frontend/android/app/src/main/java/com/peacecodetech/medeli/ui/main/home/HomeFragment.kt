package com.peacecodetech.medeli.ui.main.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.peacecodetech.medeli.R
import com.peacecodetech.medeli.databinding.CategoriesCardBinding
import com.peacecodetech.medeli.databinding.FragmentHomeBinding
import com.peacecodetech.medeli.ui.main.pharmacy.RecyclerAdapter
import com.peacecodetech.medeli.util.startAuthActivity
import timber.log.Timber


class HomeFragment : Fragment(), HomeRecyAdapter.OnViewDetail, SearchView.OnQueryTextListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val pharmacyAdapter: HomeRecyAdapter by lazy {
        HomeRecyAdapter(this){
            val bundle = Bundle().apply {
                putString("name", it.name)
                putInt("color",it.color)
            }
            navigateTo(bundle)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.searchViewId.setOnQueryTextListener(this)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding){
            add.apply {
               this.loginBtn.text = getString(R.string.add)
            }
            product.apply {
                tvId.text = getString(R.string.product)
                imagId.setImageResource(R.drawable.ic_product)
            }

            pharmacist.apply {
                tvId.text = getString(R.string.pharmacist)
                imagId.setImageResource(R.drawable.ic_pharmacy)
            }
        }
        initRecView()
    }

    private fun initRecView() {
        val recyclerView = binding.recyclerView
        recyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(),2)
            setHasFixedSize(true)
            adapter = pharmacyAdapter
        }

        pharmacyAdapter.addData(categories)
    }

    private fun navigateTo(bundle: Bundle) {
        if (findNavController().currentDestination != null)
            findNavController().navigate(R.id.action_navigation_home_to_productFragment,
                bundle)
    }



    override fun onOnViewDetail(pharmacy: Categories) {
        Toast.makeText(requireContext(), "Not yet implemented", Toast.LENGTH_LONG).show()
    }
    private val categories = arrayListOf(
        Categories(
        name = "Mom & Baby",
        color = Color.parseColor("#5D9AFF")),
        Categories(
            name = "Fighting the infection",
            color = Color.parseColor("#3CB5B7")),
        Categories(
            name = "Diabetes",
            color = Color.parseColor("#F77DB7")),
        Categories(
            name = "Antibiotics",
            color = Color.parseColor("#FE8B8A")),
        Categories(
            name = "Drugs",
            color = Color.parseColor("#FEAC7E")),
        Categories(
            name = "Women",
            color = Color.parseColor("#7879F1")),
        Categories(
            name = "Cosmetic",
            color = Color.parseColor("#5D9AFF")),
        Categories(
            name = "Men",
            color = Color.parseColor("#FFB200")),
        Categories(
            name = "Mom & Baby",
            color = Color.parseColor("#5D9AFF")),
        Categories(
            name = "Women",
            color = Color.parseColor("#7879F1")),
        Categories(
            name = "Cosmetic",
            color = Color.parseColor("#5D9AFF")),
        Categories(
            name = "Men",
            color = Color.parseColor("#FFB200")),
        Categories(
            name = "Mom & Baby",
            color = Color.parseColor("#5D9AFF")),
        Categories(
            name = "Mom & Baby",
            color = Color.parseColor("#5D9AFF")),
        Categories(
            name = "Fighting the infection",
            color = Color.parseColor("#3CB5B7")),
        Categories(
            name = "Diabetes",
            color = Color.parseColor("#F77DB7")),
        Categories(
            name = "Antibiotics",
            color = Color.parseColor("#FE8B8A")),
        Categories(
            name = "Drugs",
            color = Color.parseColor("#FEAC7E")),


    )

    override fun onQueryTextSubmit(query: String?): Boolean {
        pharmacyAdapter.filter.filter(query)
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        pharmacyAdapter.filter.filter(newText)
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

data class Categories(
    val name:String,
    val color: Int
)