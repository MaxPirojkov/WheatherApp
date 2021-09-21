package com.android.weatherapp

import com.android.weatherapp.data.network.responses.CityIdResponse
import com.android.weatherapp.data.network.responses.WeatherResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherApi {
    @GET("location/search/")
    fun getIdByName(@Query("query") query: String): Single<List<CityIdResponse>>

    @GET("location/{woeid}/")
    fun getWeatherById(@Path("woeid") cityId: Int): Single<WeatherResponse>
}