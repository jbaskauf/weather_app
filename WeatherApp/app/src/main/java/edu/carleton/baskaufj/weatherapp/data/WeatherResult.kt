package edu.carleton.baskaufj.weatherapp.data

// result generated from json

data class WeatherResult(val coord: Coord?, val weather: List<Weather>?, val base: String?, val main: Main?, val visibility: Number?, val wind: Wind?, val clouds: Clouds?, val dt: Number?, val sys: Sys?, val id: Number?, val name: String?, val cod: Number?)

data class Clouds(val all: Number?)

data class Coord(val lon: Number?, val lat: Number?)

data class Main(val temp: Number?, val pressure: Number?, val humidity: Number?, val temp_min: Number?, val temp_max: Number?)

data class Sys(val type: Number?, val id: Number?, val message: Number?, val country: String?, val sunrise: Number?, val sunset: Number?)

data class Weather(val id: Number?, val main: String?, val description: String?, val icon: String?)

data class Wind(val speed: Number?, val deg: Number?)
