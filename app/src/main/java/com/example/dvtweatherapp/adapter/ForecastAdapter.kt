package com.example.dvtweatherapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.model.Weather
import com.example.dvtweatherapp.model.WeatherList
import com.example.dvtweatherapp.utils.Constants
import com.squareup.picasso.Picasso

class ForecastAdapter:RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {
    private var daysForecast: List<WeatherList> =ArrayList()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.weather_days_list, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val daysForecast = daysForecast[position]
        holder.temperatureView.text = daysForecast.main?.temp.toString()
        holder.dateView.text = daysForecast.dt_txt
        Picasso.get().load(Constants.IMAGE_URL + daysForecast.weather?.get(0)?.icon + "@2x.png").into(holder.imageView)
    }

    fun setData(daysForecast: List<WeatherList>) {
        this.daysForecast = daysForecast
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return daysForecast.size
    }

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dateView: TextView = itemView.findViewById(R.id.day_item)
        var temperatureView: TextView = itemView.findViewById(R.id.temperature_item)
        var imageView: ImageView = itemView.findViewById(R.id.image_item)
    }
}