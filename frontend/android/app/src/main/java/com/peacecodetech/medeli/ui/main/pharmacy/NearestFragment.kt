package com.peacecodetech.medeli.ui.main.pharmacy

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.peacecodetech.medeli.R

class NearestFragment : Fragment() {

    companion object {
        fun newInstance() = NearestFragment()
    }

    private lateinit var viewModel: NearestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_nearest, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NearestViewModel::class.java)
        // TODO: Use the ViewModel
    }

}