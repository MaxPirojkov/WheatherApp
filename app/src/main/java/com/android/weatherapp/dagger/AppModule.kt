package com.android.weatherapp.dagger

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides

@Module(includes = [NetworkModule::class, WeatherModule::class])
class AppModule