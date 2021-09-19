package com.android.weatherapp.dagger

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.android.weatherapp.WeatherConverter
import com.android.weatherapp.WeatherConverterImpl
import com.android.weatherapp.WeatherInteractor
import com.android.weatherapp.WeatherInteractorImpl
import com.android.weatherapp.WeatherViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [WeatherModule.Dependencies::class])
object WeatherModule {

    @Provides
    fun providesWeatherViewModel(
        activity: AppCompatActivity,
        viewModelFactory: WeatherViewModel.WeatherViewModelFactory
    ): WeatherViewModel {
        return ViewModelProvider(activity, viewModelFactory).get(WeatherViewModel::class.java)
    }

    @Module
    interface Dependencies {

        @Binds
        fun bindsWeatherInteractor(interactor: WeatherInteractorImpl): WeatherInteractor

        @Binds
        fun bindsWeatherConverter(interactor: WeatherConverterImpl): WeatherConverter

    }
}