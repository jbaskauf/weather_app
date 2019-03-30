package edu.carleton.baskaufj.weatherapp.network


import edu.carleton.baskaufj.weatherapp.data.WeatherResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// URL: http://api.openweathermap.org/data/2.5/weather?q=Budapest,hu&units=metric&appid=3cd90055e4a084be8c9903f3d08e4d9e
// HOST: http://api.openweathermap.org/data/2.5
// PATH: /weather
// QUERY param separator: ?
// QUERY params: q, units, appid

interface WeatherAPI {
    //call the endpoint /weather and use the query param q= whatever you call getWeather() of
    //ex. getWeather("Budapest,hu") = use the query param q=Budapest,hu
    @GET("/data/2.5/weather")
    fun getWeather(@Query("q") q: String, @Query("units") units: String = "metric", @Query("appid") appId: String = "3cd90055e4a084be8c9903f3d08e4d9e") : Call<WeatherResult>
}