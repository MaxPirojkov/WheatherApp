package com.android.weatherapp

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.LifecycleOwner

interface CurrentWeatherView {
    fun bindTo(viewModel: WeatherViewModel)

    fun onDestroyView(viewModel: WeatherViewModel)
}

class CurrentWeatherViewImpl(view: View, private val lifecycleOwner: LifecycleOwner) :
    CurrentWeatherView {
    private val currentImage: ImageView = view.findViewById(R.id.image_current_wheather)
    private val currentMinTemp: TextView = view.findViewById(R.id.min_current_temp)
    private val currentMaxTemp: TextView = view.findViewById(R.id.max_current_temp)
    private val currentCardView: CardView = view.findViewById(R.id.card_current_wheather)
    private val currentTemp: TextView = view.findViewById(R.id.title_current_weather)

    override fun bindTo(viewModel: WeatherViewModel) {
        viewModel.currentWeather.observe(lifecycleOwner) {
            it?.let {
                currentCardView.visibility = View.VISIBLE
                currentImage.loadImage(it.iconUrl)
                currentMinTemp.setText(it.minTemp)
                currentMaxTemp.setText(it.maxTemp)
                currentTemp.setText(it.theTemp)
            }
        }
    }

    override fun onDestroyView(viewModel: WeatherViewModel) {
        viewModel.currentWeather.removeObservers(lifecycleOwner)
    }

}