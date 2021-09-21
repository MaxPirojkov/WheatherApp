package com.android.weatherapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.weatherapp.data.network.responses.CityIdResponse
import com.android.weatherapp.data.network.responses.WeatherDayItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.lang.IllegalArgumentException
import javax.inject.Inject

class WeatherViewModel(
    private val weatherInteractor: WeatherInteractor,
    private val weatherConverter: WeatherConverter,
) : ViewModel() {
    private var weatherSubscription: Disposable? = null

    private val _currentWeather = MutableLiveData<WeatherDayItem>()
    var currentWeather: LiveData<WeatherDayItem> = _currentWeather

    private val _progressChanges = MutableLiveData<Boolean>()
    var progressChanges: LiveData<Boolean> = _progressChanges

    private var _futureWeather = MutableLiveData<List<WeatherDayItem>>()
    var futureWeather: LiveData<List<WeatherDayItem>> = _futureWeather

    private var _errorChanges = MutableLiveData<String>()
    var errorChanges: LiveData<String> = _errorChanges

    fun getWeather(search: String) {
        weatherSubscription?.dispose()
        weatherSubscription = weatherInteractor.getWeather(search)
            .map { weatherConverter.convert(it.consolidatedWeather) }
            .doOnSubscribe { _progressChanges.value = true }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _progressChanges.value = false
                if (it.isEmpty()) {
                    _errorChanges.value = "Not receive data weather"
                } else {
                    _currentWeather.value = it[0]
                    if (it.size > 1) {
                        _futureWeather.value = it.subList(1, it.size)
                    }
                }
            }, {
                _progressChanges.value = false
                _errorChanges.value = "ERROR $it"
            })
    }

    fun onRetry(search: String) {
        getWeather(search)
    }

    override fun onCleared() {
        super.onCleared()
        weatherSubscription?.dispose()
    }

    class WeatherViewModelFactory @Inject constructor(
        private val wheatherInteractor: WeatherInteractor,
        private val weatherConverter: WeatherConverter
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {

                return WeatherViewModel(wheatherInteractor, weatherConverter) as T
            }
            throw IllegalArgumentException("Unknow viewModel class")
        }
    }
}