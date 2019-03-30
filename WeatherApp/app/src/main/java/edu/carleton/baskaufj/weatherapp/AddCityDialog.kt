package edu.carleton.baskaufj.weatherapp

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.widget.EditText
import android.widget.Toast
import edu.carleton.baskaufj.weatherapp.data.City
import edu.carleton.baskaufj.weatherapp.data.WeatherResult
import kotlinx.android.synthetic.main.dialog_add_city.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCityDialog : DialogFragment() {

    interface CityHandler {
        fun cityAdded(city: City)
        fun cityUpdated(city: City)
    }

    private lateinit var cityHandler: CityHandler

    private lateinit var etCity: EditText

    var currentIcon: String = ""
    var currentTemp: String = ""

    //attach the CityHandler to the dialog fragment
    override fun onAttach(context: Context?) {
        super.onAttach(context)

        //check if the activity is implementing the interface - if it is, then link it to the dialog fragment
        if (context is CityHandler) {
            cityHandler = context
        }
        else {
            throw RuntimeException("The Activity does not implement the CityHandler interface")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())

        builder.setTitle(getString(R.string.new_city))

        val rootView = requireActivity().layoutInflater.inflate(R.layout.dialog_add_city, null)

        etCity = rootView.etCity

        builder.setView(rootView)

        builder.setPositiveButton(getString(R.string.ok)) {
            dialog, witch -> //this event handler gets set in onResume()
        }
        return builder.create()
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        //create a new City based on the value in the dialog
        positiveButton.setOnClickListener {
            if (etCity.text.isNotEmpty()) {
                handleCityAddedWithBasicWeatherInfo(etCity.text.toString())
                //handleCityAdded()
                dialog.dismiss()
            } else {
                etCity.error = getString(R.string.empty_field)
            }
        }
    }

    private fun handleCityAdded() {
        cityHandler.cityAdded(
                //The current version of the City object also includes fields for current temp and current icon,
                //which could be updated and displayed on the main activity if it did not take too
                // many API calls to do it for each city every time the page is loaded. The values
                // are still added when the city is created but are not shown, so if they could be
                // updated more efficiently then the City object could be used as-is.
                City(null, etCity.text.toString(), currentTemp, currentIcon)
        )
    }

    //This calls the API when the City object is first created. I have left it in because it contains
    //the call to handleCityAdded, which must always be called, but must be called here specifically if we wanted
    // to show current temp and icon on the Main Activity. This shouldn't result in too many API calls
    //because it only calls the API each time a City is created (unless more than 60 are created in a minute).
    fun handleCityAddedWithBasicWeatherInfo(city: String) {
        //call the API for specific city query param
        val weatherCall = (context as MainActivity).weatherAPI.getWeather(city)
        //start doing the network communication
        weatherCall.enqueue(object : Callback<WeatherResult> {
            //throw an exception if something bad happens
            override fun onFailure(call: Call<WeatherResult>, t: Throwable) {
                //display the error message
                Toast.makeText(context, t.message, Toast.LENGTH_LONG).show()
            }
            //store the result of the query in the currentIcon and currentTemp vars based on the returned response parameter
            override fun onResponse(call: Call<WeatherResult>, response: Response<WeatherResult>) {
                val weatherResult = response.body()
                currentIcon = weatherResult?.weather?.get(0)?.icon.toString()
                currentTemp = weatherResult?.main?.temp.toString()

                //add the city to the recycler view and Room database
                handleCityAdded()

            }
        })
    }

}