package com.example.dvtweatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.databinding.FragmentHomeBinding
import com.example.dvtweatherapp.databinding.FragmentMapsBinding
import com.example.dvtweatherapp.ui.home.HomeViewModel

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}