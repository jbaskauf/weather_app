package edu.carleton.baskaufj.weatherapp.adapter

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import edu.carleton.baskaufj.weatherapp.DetailsActivity
import edu.carleton.baskaufj.weatherapp.MainActivity
import edu.carleton.baskaufj.weatherapp.R
import edu.carleton.baskaufj.weatherapp.data.AppDatabase
import edu.carleton.baskaufj.weatherapp.data.City
import edu.carleton.baskaufj.weatherapp.data.WeatherResult
import kotlinx.android.synthetic.main.city_row.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CitiesListAdapter : RecyclerView.Adapter<CitiesListAdapter.ViewHolder> {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCity = itemView.tvCity
        val tvTemp = itemView.tvTemp
        val ivIcon = itemView.ivIcon
        val btnDelete = itemView.btnDelete
    }

    companion object {
        val KEY_DATA = "KEY_DATA"
    }

    lateinit var currentCity: City

    var cities = mutableListOf<City>()

    val context : Context

    constructor(context: Context, cities: List<City>) : super() {
        this.context = context
        this.cities.addAll(cities)
    }

    constructor(context: Context) : super() {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(
                R.layout.city_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]
        currentCity = city

        //updateCityWithBasicWeatherInfo(city.name)

        holder.tvCity.text = city.name

        /* This part would show the current temperature and icon for each city on the main activity
        (these lines as well as the function updateCityWithBasicWeatherInfo)
        However, it results in too many API calls for my free key so I have turned this part off.
        The function updateCityWithBasicWeatherInfo doesn't work correctly as far as I can tell,
        but even without that function, these lines seem to work fine. However, without that function,
        they would only show the current weather at the time the City object was created, which would
        later become the wrong weather, so I have not included these lines either. But I have left the
        Views in the city_row.xml (with visibility = gone) to document how this code would work.

        //if trying to show the current weather for each city on the main activity, make these elements of the city row visible
        holder.ivIcon.visibility = View.VISIBLE
        holder.tvTemp.visibility = View.VISIBLE

        //change icon based on weather
        Glide.with(context)
                .load(
                        ("https://openweathermap.org/img/w/" + city.weatherIcon + ".png"))
                .into(holder.ivIcon)

        holder.tvTemp.text = "${city.currentTemp}°C"
        */

        holder.btnDelete.setOnClickListener {
            deleteCity(holder.adapterPosition)
        }

        //if you click anywhere on the card, edit the item
        holder.itemView.setOnClickListener {

            //open the city details activity
            var intentStart = Intent()
            intentStart.setClass(context, DetailsActivity::class.java)
            intentStart.putExtra(KEY_DATA, city.name)

            context.startActivity(intentStart)
        }
    }

    fun addCity(city: City) {
        cities.add(city)
        notifyItemInserted(cities.lastIndex)
    }

    fun updateCity(city: City, idx: Int) {
        cities[idx] = city
        notifyItemChanged(idx)
    }

    private fun deleteCity(adapterPosition: Int) {
        //remove from the database
        Thread {
            AppDatabase.getInstance(context).citiesListDao().deleteCity(cities[adapterPosition])

            //remove from the recycler view
            cities.removeAt(adapterPosition)
            (context as MainActivity).runOnUiThread {
                notifyItemRemoved(adapterPosition)
            }
        }.start()
    }

    //This doesn't work for some reason? It doesn't actually seem to update the weather info after
    //the City item is first created. It does make the API calls, however, which results in too many
    // API calls for my free key, so I have not included it.
    /*fun updateCityWithBasicWeatherInfo(city: String) {
        //call the API for specific city query param
        val weatherCall = (context as MainActivity).weatherAPI.getWeather(city)
        //start doing the network communication
        weatherCall.enqueue(object : Callback<WeatherResult> {
            //throw an exception if something bad happens
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                //display the error message
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
            //show the result of the query in a toast based on the returned response parameter
            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                currentCity.weatherIcon = weatherResult?.weather?.get(0)?.icon.toString()
                currentCity.currentTemp = weatherResult?.main?.temp.toString()
                Thread{
                    AppDatabase.getInstance(context).citiesListDao().updateCity(currentCity)
                }
            }
        })
    }*/

}