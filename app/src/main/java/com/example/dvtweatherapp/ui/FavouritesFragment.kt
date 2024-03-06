package com.example.dvtweatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.adapter.FavouritesAdapter
import com.example.dvtweatherapp.database.FavouriteTable
import com.example.dvtweatherapp.databinding.FragmentFavouritesBinding
import com.example.dvtweatherapp.viewmodel.FavouriteViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouritesFragment : Fragment() {
    private var _binding: FragmentFavouritesBinding? = null
    private var adapter: FavouritesAdapter? = null
    private val favouriteViewModel by viewModel<FavouriteViewModel>()
    private var backgroundTheme: ConstraintLayout? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater, container, false)
        val root: View = binding.root
        backgroundTheme = binding.favouriteContent
        val recyclerView: RecyclerView = binding.favouriteRecyclerview

        favouriteViewModel.getOfflineWeather()?.observe(viewLifecycleOwner) {
            if (it != null) {
                when {
                    it.description.contains("Cloud") -> {
                        backgroundTheme?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudy_color
                            )
                        )
                    }

                    it.description.contains("Rain") -> {
                        backgroundTheme?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainy_color
                            )
                        )
                    }

                    it.description.contains("Sun") -> {
                        backgroundTheme?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunny_color
                            )
                        )
                    }
                }
            }
        }

        favouriteViewModel.getFavourites()?.observe(viewLifecycleOwner) {
            if (it != null) {
                adapter = FavouritesAdapter(requireContext())
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter
                adapter?.setData(it)
                val linearLayoutManager = LinearLayoutManager(requireActivity())
                recyclerView.layoutManager = linearLayoutManager
                adapter?.setOnClickListener(object :
                    FavouritesAdapter.OnClickListener {
                    override fun onClick(position: Int, model: FavouriteTable) {
                        val bundle = bundleOf("place" to model.place)
                        findNavController().navigate(
                            R.id.action_favourites_to_favourite_details,
                            bundle
                        )
                    }
                })
            }
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}