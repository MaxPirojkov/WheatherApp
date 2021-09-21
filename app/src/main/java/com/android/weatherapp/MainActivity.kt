package com.android.weatherapp

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.weatherapp.dagger.DaggerAppComponent
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: WeatherViewModel

    private var fusedLocationProviderClient: FusedLocationProviderClient? = null
    private var cityInput: EditText? = null
    private var currentWeatherView: CurrentWeatherView? = null
    private var adapter: WeatherAdapter? = null
    private var progressBar: ProgressBar? = null
    private var contentGroup: Group? = null
    private var errorGroup: Group? = null
    private var errorText: TextView? = null
    private var searchButton: Button? = null
    private var myLocation: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerAppComponent.factory().create(this).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        initViews()

        bindTo(viewModel)
    }


    override fun onDestroy() {
        super.onDestroy()
        currentWeatherView?.onDestroyView(viewModel)
    }

    private fun bindTo(viewModel: WeatherViewModel) {
        viewModel.futureWeather.observe(this) {
            it?.let {
                adapter?.updates(it)
            }
        }
        viewModel.progressChanges.observe(this) { showProgress ->
            showProgress?.let {
                if (showProgress) {
                    searchButton?.isEnabled = false
                    myLocation?.isEnabled = false
                    progressBar?.visibility = View.VISIBLE
                    contentGroup?.visibility = View.GONE
                    errorGroup?.visibility = View.GONE
                } else {
                    progressBar?.visibility = View.GONE
                    contentGroup?.visibility = View.VISIBLE
                    searchButton?.isEnabled = true
                    myLocation?.isEnabled = true
                }
            }
        }
        viewModel.errorChanges.observe(this) { error ->
            error?.let {
                errorGroup?.visibility = View.VISIBLE
                errorText?.text = error
                contentGroup?.visibility = View.GONE

            }
        }
    }

    private fun initViews() {
        cityInput = findViewById(R.id.city_input)
        progressBar = findViewById(R.id.progress_bar)
        contentGroup = findViewById(R.id.content_group)
        errorGroup = findViewById(R.id.error_group)
        errorText = findViewById(R.id.error_text)
        currentWeatherView = CurrentWeatherViewImpl(findViewById(R.id.card_current_wheather), this)
        currentWeatherView?.bindTo(viewModel)
        searchButton = findViewById(R.id.search_button)
        myLocation = findViewById(R.id.my_location_button)
        val retryButton: Button = findViewById(R.id.retry_button)
        initRecycler()

        retryButton.setOnClickListener {
            viewModel.onRetry(cityInput?.text.toString())
        }

        myLocation?.setOnClickListener {
            fetchLocation()
            hideKeyboard()
        }

        searchButton?.setOnClickListener {
            searchWeather()
            hideKeyboard()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                fetchLocation()
            } else {
                showToast("Need permission for use location")
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun initRecycler() {
        val recyclerView = findViewById<RecyclerView>(R.id.rc_view)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = WeatherAdapter()
        recyclerView.adapter = adapter
    }

    private fun searchWeather() {
        val fromEditText: String = cityInput?.text.toString()
        if (fromEditText.isNotEmpty()) {
            viewModel.getWeather(fromEditText)
        } else {
            showToast("Please enter the City")
        }
    }

    private fun fetchLocation() {
        val task = fusedLocationProviderClient?.lastLocation
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
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
            return
        }
        task?.addOnSuccessListener {
            fusedLocationProviderClient?.lastLocation
            cityInput?.let { input ->
                input.setText(getAddress(it.latitude, it.longitude))
                input.setSelection(input.length())
            }
        }
    }

    private fun getAddress(latitude: Double, longitude: Double): String {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val list: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            val city: String = list[0].locality
            viewModel.getWeather(city)
            city
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}

private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101

