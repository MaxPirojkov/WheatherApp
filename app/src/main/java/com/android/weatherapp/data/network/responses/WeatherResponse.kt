package com.android.weatherapp.data.network.responses

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("consolidated_weather") val consolidatedWeather: List<WeatherDay>
)

data class WeatherDay(
    @SerializedName("min_temp") val minTemp: Float,
    @SerializedName("max_temp") val maxTemp: Float,
    @SerializedName("the_temp") val theTemp: Float,
    @SerializedName("weather_state_abbr") val weatherAbbr: String,
    @SerializedName("applicable_date") val applicableDate: String
)

data class WeatherDayItem(
    val minTemp: Float,
    val maxTemp: Float,
    val theTemp: Float,
    val weatherAbbr: String,
    val applicableDate: String,
    val iconUrl: String
)
