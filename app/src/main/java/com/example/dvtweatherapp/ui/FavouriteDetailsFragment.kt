package com.example.dvtweatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.databinding.FragmentFavouriteDetailsBinding
import com.example.dvtweatherapp.databinding.FragmentFavouritesBinding
import com.example.dvtweatherapp.model.WeatherResults
import com.example.dvtweatherapp.utils.ErrorHandlerUtil
import com.example.dvtweatherapp.viewmodel.WeatherViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavouriteDetailsFragment : Fragment() {
    private val weatherViewModel by viewModel<WeatherViewModel>()
    private var errorHandlerUtil: ErrorHandlerUtil? = null
    private var _binding: FragmentFavouriteDetailsBinding? = null
    private var backgroundTheme: ConstraintLayout? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavouriteDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        backgroundTheme = binding.favouriteDetailsBackground
        errorHandlerUtil = ErrorHandlerUtil()
        val place = arguments?.getString("place")
        weatherViewModel.getCurrentWeather(place).observe(viewLifecycleOwner) { response ->
            val error = response as? WeatherResults.Error
            if (error != null) {
                errorHandlerUtil?.connectionError(error.error, requireContext())
            }

            val success = (response as? WeatherResults.SuccessResults)?.data
            success?.let {
                val main = it.weather[0].main
                when {
                    main?.contains("Cloud") == true -> {
                        backgroundTheme?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.cloudy_color
                            )
                        )
                    }

                    main?.contains("Rain") == true -> {
                        backgroundTheme?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.rainy_color
                            )
                        )
                    }

                    main?.contains("Sun") == true -> {
                        backgroundTheme?.setBackgroundColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.sunny_color
                            )
                        )
                    }
                }

                binding.favouriteDescription.text = it.weather[0].description
                binding.favouritePlaceDetails.text = place
                binding.favouriteTemp.text = "${it.main?.temp?.toInt().toString().take(2)}°"
            }
        }
        return root
    }
}