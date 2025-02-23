package com.example.weatherapplication.FirstPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapplication.R
import com.example.weatherapplication.roomdb.City


class CityAdapter(private val onItemClick: (City) -> Unit) : ListAdapter<City, CityAdapter.CityViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        val city = getItem(position)
        holder.bind(city)
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val cityNameTextView: TextView = itemView.findViewById(R.id.cityNameTextView)

        fun bind(city: City) {
            cityNameTextView.text = city.cityName
            itemView.setOnClickListener { onItemClick(city) }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<City>() {
        override fun areItemsTheSame(oldItem: City, newItem: City): Boolean = oldItem.cityName == newItem.cityName
        override fun areContentsTheSame(oldItem: City, newItem: City): Boolean = oldItem == newItem
    }
}
