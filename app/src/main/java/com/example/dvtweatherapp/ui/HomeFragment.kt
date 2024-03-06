package com.example.dvtweatherapp.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.adapter.ForecastAdapter
import com.example.dvtweatherapp.database.FavouriteTable
import com.example.dvtweatherapp.database.OfflineForecastTable
import com.example.dvtweatherapp.database.OfflineTable
import com.example.dvtweatherapp.databinding.FragmentHomeBinding
import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.WeatherResults
import com.example.dvtweatherapp.model.currentWeather.CurrentWeatherModel
import com.example.dvtweatherapp.utils.Constants
import com.example.dvtweatherapp.viewmodel.FavouriteViewModel
import com.example.dvtweatherapp.viewmodel.WeatherViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale


class HomeFragment : Fragment() {
    private val viewModel by viewModel<WeatherViewModel>()
    private val favouriteViewModel by viewModel<FavouriteViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private var adapter: ForecastAdapter? = null
    private var weatherData: Weather? = null
    private var currentWeatherModelData: CurrentWeatherModel? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val binding get() = _binding!!
    private var alert: Dialog? = null
    private var minTemp: TextView? = null
    private var currentTemp: TextView? = null
    private var maxTemp: TextView? = null
    private var image: ImageView? = null
    private var recyclerView: RecyclerView? = null
    private var descriptionText: TextView? = null
    private var mainTempText: TextView? = null
    private var cityName: String? = null
    private var backgroundTheme: ConstraintLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!Places.isInitialized()) {
            Places.initialize(this.requireContext(), Constants.PLACES_ID)
        }
        Places.createClient(this.requireContext())
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        minTemp = binding.minTemp
        currentTemp = binding.currentTemp
        maxTemp = binding.maxTemp
        descriptionText = binding.description
        image = binding.imageView
        recyclerView = binding.favouritesRecyclerview
        mainTempText = binding.textView5
        backgroundTheme = binding.content

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        Dexter.withContext(requireActivity())
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    getLocation()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    TODO("Not yet implemented")
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissionRequest: PermissionRequest?,
                    permissionToken: PermissionToken?
                ) {
                    permissionToken?.continuePermissionRequest()
                }

            }).check()
        return root
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            return
        }
        val task = fusedLocationClient.lastLocation
        task?.addOnSuccessListener { location ->
            val geocoder = Geocoder(requireContext(), Locale.getDefault())

            val addresses: List<Address>? =
                geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val city: String? = addresses?.get(0)?.locality
            cityName = city
            fetchWeather(city)
        }
    }

    private fun fetchWeather(place: String?) {
        viewModel.getForecast(place).observe(viewLifecycleOwner) { response ->
            val error = response as? WeatherResults.Error
            if (error != null) {
                connectionError(error.error, requireContext())
            }

            val success = (response as? WeatherResults.SuccessResults)?.data
            success?.let {
                weatherData = it
                adapter = ForecastAdapter(requireContext())
                recyclerView?.setHasFixedSize(true)
                recyclerView?.adapter = adapter
                it.list?.get(0)?.weather?.get(0)?.main
                it.list?.let { it1 -> adapter?.setData(it1) }
                for (i in it.list?.indices!!) {
                    favouriteViewModel.addOfflineForecast(
                        OfflineForecastTable(
                            it.list!![i].dt_txt,
                            it.main?.temp_min.toString(),
                            it.main?.temp_min?.toInt().toString()
                        )
                    )
                }

                val linearLayoutManager = LinearLayoutManager(requireActivity())
                recyclerView?.layoutManager = linearLayoutManager
            }
        }

        viewModel.getCurrentWeather(place).observe(viewLifecycleOwner) { response ->
            val error = response as? WeatherResults.Error
            if (error != null) {
                connectionError(error.error, requireContext())
            }

            val success = (response as? WeatherResults.SuccessResults)?.data
            success?.let {
                currentWeatherModelData = it
                val maxTemp = it.main?.temp_max?.toString()?.take(2) + " °\n" + "min "
                val minTemp = it.main?.temp_min?.toString()?.take(2) + " °\n" + "min "
                val currentTemp = it.main?.temp?.toString()?.take(2)
                val description = it.weather[0].description
                val main = it.weather[0].main

                if (main?.contains("Cloud") == true) {
                    backgroundTheme?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.cloudy_color
                        )
                    )
                    image?.setImageResource(R.drawable.forest_cloudy)
                } else if (main?.contains("Rain") == true) {
                    backgroundTheme?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.rainy_color
                        )
                    )
                    image?.setImageResource(R.drawable.forest_rainy)
                } else {
                    backgroundTheme?.setBackgroundColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.sunny_color
                        )
                    )
                    image?.setImageResource(R.drawable.forest_sunny)
                }

                if (place != null && description != null) {
                    favouriteViewModel.addOfflineWeather(
                        OfflineTable(
                            currentTemp.toString(),
                            minTemp,
                            maxTemp,
                            description,
                            place
                        )
                    )
                }
                initView(maxTemp, minTemp, currentTemp, description)
            }
        }
    }

    private fun initView(
        maxTemperature: String,
        minTemperature: String,
        currentTemperature: String?,
        description: String?
    ) {
        maxTemp?.text = maxTemperature
        minTemp?.text = minTemperature
        currentTemp?.text = currentTemperature + " °\n" + "Current "
        descriptionText?.text = description
        mainTempText?.text = currentTemperature + "°"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun connectionError(throwable: Throwable, context: Context) {
        val showing = alert?.isShowing ?: false
        if (showing)
            return
        val message = throwable.toString()

        val title: String
        val content: String
        when {
            message.contains("java.net.UnknownHostException", true) -> {
                title = "Internet Not Available"
                content =
                    "Could not connect to the Internet. Please verify that you are connected and try again"
            }

            message.contains("java.net.SocketTimeoutException", true) -> {
                title = "Connection Timeout"
                content =
                    "Server took too long to respond. This may be caused by a bad network connection"
            }

            message.contains("javax.net.ssl.SSLPeerUnverifiedException", true) -> {
                title = "SSL Cert. Unverified"
                content = "Hostname not verified"
            }

            else -> {
                title = "Unknown Error"
                content = "An Unknown error has occurred. Please try again later"
            }
        }

        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
            .setMessage(content)
            .setCancelable(true)
            .setNegativeButton("Go Offline") { dialog: DialogInterface?, _: Int ->
                //pull from database
                favouriteViewModel.getOfflineForecast()?.observe(viewLifecycleOwner) { forecast ->
                    if (forecast != null) {
                        favouriteViewModel.getOfflineWeather()
                            ?.observe(viewLifecycleOwner) { offlineWeather ->
                                initView(
                                    offlineWeather.maxTemp,
                                    offlineWeather.minTemp,
                                    offlineWeather.temp,
                                    offlineWeather.description
                                )
                                Toast.makeText(context, "Offline", Toast.LENGTH_LONG).show()
                            }
                    } else {
                        Toast.makeText(context, "No saved weather", Toast.LENGTH_LONG).show()
                    }
                }
                dialog?.dismiss()
            }
            .setPositiveButton("Retry") { dialog: DialogInterface?, _: Int ->
                fetchWeather(cityName)
                dialog?.dismiss()
            }

        alert = builder.create()
        alert?.show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val favouriteTable = FavouriteTable(
                    currentWeatherModelData?.name.toString(),
                    currentWeatherModelData?.main?.temp_min.toString(),
                    currentWeatherModelData?.main?.temp_max.toString(),
                    currentWeatherModelData?.weather?.get(0)?.main
                )
                favouriteViewModel.addFavourite(favouriteTable)
                true
            }

            R.id.action_add_city -> {
                addCity()
                true
            }

            else -> false
        }
    }

    private fun addCity() {
        val fields: List<Place.Field> = listOf(
            Place.Field.ID,
            Place.Field.NAME,
            Place.Field.ADDRESS,
            Place.Field.LAT_LNG
        )
        val intent = Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
            .build(this.requireContext())
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
                val place = Autocomplete.getPlaceFromIntent(data)

                if (place.name != null) {
                    fetchWeather(place.name)
                    //save to database
                    findNavController().navigateUp()
                }
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                val status = Autocomplete.getStatusFromIntent(data)
                Toast.makeText(context, status.statusMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}