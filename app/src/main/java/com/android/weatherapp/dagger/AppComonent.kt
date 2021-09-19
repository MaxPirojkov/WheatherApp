package com.android.weatherapp.dagger

import androidx.appcompat.app.AppCompatActivity
import com.android.weatherapp.MainActivity
import dagger.BindsInstance
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance activity: AppCompatActivity): AppComponent
    }

}

