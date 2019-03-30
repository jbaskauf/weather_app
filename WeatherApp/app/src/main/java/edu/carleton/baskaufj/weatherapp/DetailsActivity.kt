package edu.carleton.baskaufj.weatherapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import edu.carleton.baskaufj.weatherapp.adapter.CitiesListAdapter
import edu.carleton.baskaufj.weatherapp.adapter.CitiesListAdapter.Companion.KEY_DATA
import edu.carleton.baskaufj.weatherapp.data.WeatherResult
import edu.carleton.baskaufj.weatherapp.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_details.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DetailsActivity : AppCompatActivity() {

    lateinit var weatherAPI: WeatherAPI
    private val HOST_URL = "https://api.openweathermap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)

        if (intent.hasExtra(CitiesListAdapter.KEY_DATA)) {
            tvCityName.text = intent.getStringExtra(CitiesListAdapter.KEY_DATA)
        }

        btnHome.setOnClickListener {
            var intentBack = Intent()
            intentBack.setClass(DetailsActivity@this, MainActivity::class.java)
            startActivity(intentBack)
            finish()
        }

        initRetrofit()

        makeAPICall(tvCityName.text.toString())
    }

    private fun initRetrofit() {
        //retrofit represents the host url
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //identify the endpoints
        weatherAPI = retrofit.create(WeatherAPI::class.java)
    }

    fun makeAPICall(city: String) {
        //call the API for specific city query param
        val weatherCall = weatherAPI.getWeather(city)
        //start doing the network communication
        weatherCall.enqueue(object : Callback<WeatherResult> {
            //throw an exception if something bad happens
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                //display the error message
                Toast.makeText(this@DetailsActivity, t.message, Toast.LENGTH_LONG).show()
            }
            //show the result of the query in the text views based on the returned response parameter
            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                if (weatherResult == null) {
                    setTvValuesForNullResponseResult()
                }
                else {
                    resetTvDefaultSettings()
                    setTvValuesWithWeatherData(weatherResult)
                }
            }
        })
    }

    private fun setTvValuesWithWeatherData(weatherResult: WeatherResult?) {
        //change icon based on weather
        Glide.with(this@DetailsActivity).load("https://openweathermap.org/img/w/" +
                weatherResult?.weather?.get(0)?.icon
                + ".png").into(weatherIcon)

        tvTemperature.text = String.format(getResources().getString(R.string.temp), weatherResult?.main?.temp?.toString())
        tvWeatherDescription.text = weatherResult?.weather?.get(0)?.description.toString()
        tvMaxTemp.text = String.format(getResources().getString(R.string.max_temp), weatherResult?.main?.temp_max?.toString())
        tvMinTemp.text = String.format(getResources().getString(R.string.min_temp), weatherResult?.main?.temp_min?.toString())
        tvHumidity.text = String.format(getResources().getString(R.string.humidity), weatherResult?.main?.humidity?.toString())
        tvPressure.text = String.format(getResources().getString(R.string.pressure), weatherResult?.main?.pressure?.toString())
    }

    private fun resetTvDefaultSettings() {
        tvTemperature.textSize = 40F
        tvWeatherDescription.textSize = 25F
        weatherIcon.visibility = View.VISIBLE
        tvMaxTemp.visibility = View.VISIBLE
        tvMinTemp.visibility = View.VISIBLE
        tvPressure.visibility = View.VISIBLE
        tvHumidity.visibility = View.VISIBLE
    }

    private fun setTvValuesForNullResponseResult() {
        tvTemperature.text = getString(R.string.no_info_available)
        tvTemperature.textSize = 25F
        tvWeatherDescription.text = getString(R.string.did_you_misspell)
        tvWeatherDescription.textSize = 20F
        weatherIcon.visibility = View.GONE
        tvMaxTemp.visibility = View.GONE
        tvMinTemp.visibility = View.GONE
        tvPressure.visibility = View.GONE
        tvHumidity.visibility = View.GONE
    }
}
