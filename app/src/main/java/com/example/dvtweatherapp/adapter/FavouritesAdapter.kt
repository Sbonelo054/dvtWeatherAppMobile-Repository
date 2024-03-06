package com.example.dvtweatherapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.dvtweatherapp.R
import com.example.dvtweatherapp.database.FavouriteTable

class FavouritesAdapter(var context: Context) :
    RecyclerView.Adapter<FavouritesAdapter.FavouritesViewHolder>() {
    var favourites: List<FavouriteTable> = ArrayList()
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouritesAdapter.FavouritesViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourites_list, parent, false)
        return FavouritesViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavouritesAdapter.FavouritesViewHolder, position: Int) {
        val favourite = favourites[position]
        if (favourite.description?.contains("Cloud") == true) {
            holder.favouriteBackgroud.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.cloudy_color
                )
            )
        } else if (favourite.description?.contains("Rain") == true) {
            holder.favouriteBackgroud.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.rainy_color
                )
            )
        } else {
            holder.favouriteBackgroud.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.sunny_color
                )
            )
        }

        holder.minMaxTemperature.text =
            "${favourite.maxTemp.take(2)}°/${favourite.minTemp.take(2)}°"
        holder.favouritePlace.text = favourite.place
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, favourite)
            }
        }
    }

    fun setData(favourites: List<FavouriteTable>) {
        this.favourites = favourites
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return favourites.size
    }

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int, model: FavouriteTable)
    }

    inner class FavouritesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var minMaxTemperature: TextView = itemView.findViewById(R.id.favourite_temperature_item)
        var favouritePlace: TextView = itemView.findViewById(R.id.favourite_place_item)
        var favouriteBackgroud: ConstraintLayout = itemView.findViewById(R.id.favourite_list_item)
    }
}