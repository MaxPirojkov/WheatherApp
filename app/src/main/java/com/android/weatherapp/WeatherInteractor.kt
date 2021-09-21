package com.android.weatherapp

import com.android.weatherapp.data.network.responses.CityIdResponse
import com.android.weatherapp.data.network.responses.WeatherResponse
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

interface WeatherInteractor {
    fun getWeather(query: String): Single<WeatherResponse>

}

class WeatherInteractorImpl @Inject constructor(private val api: WeatherApi) : WeatherInteractor {
    override fun getWeather(query: String): Single<WeatherResponse> {
        return api.getIdByName(query)
            .map { it.first() }
            .flatMap { api.getWeatherById(it.cityId) }
            .subscribeOn(Schedulers.io())

    }

}



