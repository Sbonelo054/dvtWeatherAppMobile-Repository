package com.example.dvtweatherapp.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.databinding.FragmentMapsBinding
import com.example.dvtweatherapp.viewmodel.FavouriteViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class MapsFragment : Fragment() {
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private var supportMapFragment: SupportMapFragment? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val favouriteViewModel by viewModel<FavouriteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        supportMapFragment =
            (childFragmentManager.findFragmentById(R.id.maps_container) as SupportMapFragment?)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
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

    private fun getLocation() {
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
        val task = fusedLocationClient?.lastLocation
        task?.addOnSuccessListener { location ->
            supportMapFragment?.getMapAsync(object : OnMapReadyCallback {
                override fun onMapReady(googleMap: GoogleMap) {
                    if (location != null) {
                        favouriteViewModel.getFavourites()?.observe(viewLifecycleOwner) {
                            if (it != null) {
                                for (place in it) {
                                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                                    val addresses: List<Address>? =
                                        geocoder.getFromLocationName(place.place, 10)
                                    val lat: Double? = addresses?.get(0)?.latitude
                                    val lon: Double? = addresses?.get(0)?.longitude

                                    if (lat != null && lon != null) {
                                        val favLatLon = LatLng(lat, lon)
                                        val locationArrayList: ArrayList<LatLng> = ArrayList()
                                        locationArrayList.add(favLatLon)
                                        for (i in locationArrayList.indices) {
                                            googleMap.addMarker(
                                                MarkerOptions().position(
                                                    locationArrayList[i]
                                                ).title("Favourites")
                                            )
                                            googleMap.animateCamera(CameraUpdateFactory.zoomTo(18.0f))
                                            googleMap.moveCamera(
                                                CameraUpdateFactory.newLatLng(
                                                    locationArrayList[i]
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }
            })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}