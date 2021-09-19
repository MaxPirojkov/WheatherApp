package com.android.weatherapp

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.dagger.DaggerAppComponent
import com.android.wheatherapp.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: WeatherViewModel

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var cityEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.factory().create(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        cityEdit = findViewById(R.id.edit_location)
        val searchButton: Button = findViewById(R.id.search_button)
        val myLocation: Button = findViewById(R.id.my_location_button)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        myLocation.setOnClickListener {
            fetchLocation()
        }



        searchButton.setOnClickListener {
            searchWeather()
        }


    }

    private fun searchWeather() {
        val fromEditText: String = cityEdit.text.toString()
        if (fromEditText != "") {
            viewModel.onSave(fromEditText)
            val recyclerView = findViewById<RecyclerView>(R.id.rc_view)
            recyclerView.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            var adapter = RecyclerAdapter(this)
            recyclerView.adapter = adapter
            viewModel.futureWeather.observe(this, Observer {
                it?.let {
                    adapter.updates(it)
                }
            })
        } else {
            Toast.makeText(this, "Please enter the City", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                Toast.makeText(
                    applicationContext,
                    "${it.longitude} ${it.latitude}",
                    Toast.LENGTH_SHORT
                ).show()
                val task = fusedLocationProviderClient.lastLocation
                findViewById<EditText>(R.id.edit_location).hint =
                    getAddress(it.latitude, it.longitude)
            }
        }

    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        var currentCity: String = ""
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            var list: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (list != null) {
                var city: String = list[0].locality
                if (city != null) {
                    currentCity = city
                } else {
                    Log.e("TAG", "CITY NOT FOUND")
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return currentCity
    }

}

