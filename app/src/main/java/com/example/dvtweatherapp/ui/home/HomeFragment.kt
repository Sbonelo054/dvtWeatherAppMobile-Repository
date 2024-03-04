package com.example.dvtweatherapp.ui.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.adapter.ForecastAdapter
import com.example.dvtweatherapp.databinding.FragmentFavouritesBinding
import com.example.dvtweatherapp.databinding.FragmentHomeBinding
import com.example.dvtweatherapp.model.WeatherResults
import com.example.dvtweatherapp.ui.gallery.GalleryViewModel
import com.example.dvtweatherapp.utils.Constants
import com.example.dvtweatherapp.viewmodel.WeatherViewModel
import com.squareup.picasso.Picasso
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {
    private val viewModel by viewModel<WeatherViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private var adapter : ForecastAdapter? = null
    private val binding get() = _binding!!
    private var alert : Dialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val minTemp: TextView = binding.minTemp
        val currentTemp: TextView = binding.currentTemp
        val maxTemp: TextView = binding.maxTemp
        val image: ImageView = binding.imageView
        val recyclerView: RecyclerView = binding.favouritesRecyclerview

        viewModel.getCurrentWeather("Midrand").observe(viewLifecycleOwner) { response ->
            val error = response as? WeatherResults.Error
            if (error != null) {
                connectionError(error.error)
            }

            val success = (response as? WeatherResults.SuccessResults)?.data
            success?.let {
                Picasso.get().load(Constants.IMAGE_URL + it.list?.get(0)?.weather?.get(0)?.icon + "@2x.png").into(image)
                val mainTemp = "${it.list?.get(0)?.main?.temp_max?.toInt().toString()} °"
                maxTemp.text = it.cod
                val discription = it.list?.get(0)?.weather?.get(0)?.main
                val firstTempData = "${it.list?.get(1)?.main?.temp_max?.toInt().toString()} °"
                val secondTempData = "${it.list?.get(2)?.main?.temp_max?.toInt().toString()} °"
                val thirdTempData = "${it.list?.get(3)?.main?.temp_max?.toInt().toString()} °"
                val fourthTempData = "${it.list?.get(4)?.main?.temp_max?.toInt().toString()} °"
            }
        }

        viewModel.getForecast("Midrand","").observe(viewLifecycleOwner) { response->
            Toast.makeText(context,response.toString(),Toast.LENGTH_LONG).show()
            val error = response as? WeatherResults.Error
            if (error != null) {
                connectionError(error.error)
            }

            val success = (response as? WeatherResults.SuccessResults)?.data
            success?.let {
                adapter = ForecastAdapter()
                recyclerView.setHasFixedSize(true)
                recyclerView.adapter = adapter
                it.list?.let { it1 -> adapter?.setData(it1) }
                val linearLayoutManager = LinearLayoutManager(requireActivity())
                recyclerView.layoutManager = linearLayoutManager
            }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun connectionError(throwable: Throwable) {
        val showing = alert?.isShowing ?: false
        if(showing)
            return
        val message= throwable.toString()

        val title: String
        val content: String
        when {
            message.contains("java.net.UnknownHostException",true) -> {
                title =  "Internet Not Available"
                content = "Could not connect to the Internet. Please verify that you are connected and try again"
            }
            message.contains("java.net.SocketTimeoutException",true) -> {
                title =  "Connection Timeout"
                content = "Server took too long to respond. This may be caused by a bad network connection"
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
            .setPositiveButton("Retry") { dialog: DialogInterface?, _: Int ->
                //getHistory()
                dialog?.dismiss()
            }

        alert = builder.create()
        alert?.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}