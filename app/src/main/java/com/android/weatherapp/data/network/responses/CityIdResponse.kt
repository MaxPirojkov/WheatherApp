package com.android.weatherapp.data.network.responses

import com.google.gson.annotations.SerializedName


data class CityIdResponse(
    @SerializedName("woeid") val cityId: Int,
)



