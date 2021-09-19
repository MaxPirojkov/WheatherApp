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
                minTemp = it.minTemp,
                maxTemp = it.maxTemp,
                applicableDate = it.applicableDate,
                theTemp = it.theTemp,
                weatherAbbr = it.weatherAbbr,
                iconUrl = "$ICON_URL${it.weatherAbbr}.svg"
            )
        }
    }
}

private const val ICON_URL = "https://www.metaweather.com/static/img/weather/"

