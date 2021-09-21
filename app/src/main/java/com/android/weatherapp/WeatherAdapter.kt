package com.android.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.data.network.responses.WeatherDayItem

class WeatherAdapter() : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {
    private var weatherList: List<WeatherDayItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preview_weather_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(weatherList.get(position))
    }

    override fun getItemCount(): Int = weatherList.size

    inner class ViewHolder(item: View) : RecyclerView.ViewHolder(item) {
        val itemImage: ImageView = item.findViewById(R.id.image_wheather)
        val itemDate: TextView = item.findViewById(R.id.title_date_weather)
        val itemMinTemp: TextView = item.findViewById(R.id.min_temp_item)
        val itemMaxTemp: TextView = item.findViewById(R.id.max_temp_item)

        fun bind(previewWeather: WeatherDayItem) {
            itemImage.loadImage(previewWeather.iconUrl)
            itemDate.setText(previewWeather.applicableDate)
            itemMinTemp.setText(previewWeather.minTemp)
            itemMaxTemp.setText(previewWeather.maxTemp)
        }
    }

    fun updates(weatherList: List<WeatherDayItem>) {
        this.weatherList = weatherList
        notifyDataSetChanged()
    }


}