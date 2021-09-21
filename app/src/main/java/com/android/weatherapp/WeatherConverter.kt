package com.android.weatherapp

import com.android.weatherapp.data.network.responses.WeatherDay
import com.android.weatherapp.data.network.responses.WeatherDayItem
import javax.inject.Inject

interface WeatherConverter {
    fun convert(items: List<WeatherDay>): List<WeatherDayItem>
}

class WeatherConverterImpl @Inject constructor() :
    WeatherConverter {

    override fun convert(items: List<WeatherDay>): List<WeatherDayItem> {
        return items.map {
            WeatherDayItem(
                minTemp = "Min temperature: ${String.format("%.2f", it.minTemp)}",
                maxTemp = "Max temperature: ${String.format("%.2f", it.maxTemp)}",
                applicableDate = "Date: ${it.applicableDate}",
                weatherAbbr = it.weatherAbbr,
                theTemp = "Current weather: ${String.format("%.2f", it.theTemp)}",
                iconUrl = "$ICON_URL${it.weatherAbbr}.png"
            )
        }
    }
}

private const val ICON_URL = "https://www.metaweather.com/static/img/weather/png/64/"

