package com.android.weatherapp.dagger

import dagger.Module

@Module(includes = [NetworkModule::class, WeatherModule::class])
class AppModule