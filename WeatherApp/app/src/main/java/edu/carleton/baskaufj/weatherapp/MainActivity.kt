package edu.carleton.baskaufj.weatherapp

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import edu.carleton.baskaufj.weatherapp.adapter.CitiesListAdapter
import edu.carleton.baskaufj.weatherapp.data.AppDatabase
import edu.carleton.baskaufj.weatherapp.data.City
import edu.carleton.baskaufj.weatherapp.data.WeatherResult
import edu.carleton.baskaufj.weatherapp.network.WeatherAPI
import kotlinx.android.synthetic.main.activity_details.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, AddCityDialog.CityHandler {

    lateinit var citiesListAdapter: CitiesListAdapter

    //remember which item is under edit mode
    private var editIndex: Int = 0

    lateinit var weatherAPI: WeatherAPI
    private val HOST_URL = "https://api.openweathermap.org/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            showAddCityDialog()
        }

        initNavDrawer()
        initRetrofit()
        initRecyclerView()
    }

    private fun initRetrofit() {
        val retrofit = Retrofit.Builder()
                .baseUrl(HOST_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        //identify the endpoints
        weatherAPI = retrofit.create(WeatherAPI::class.java)
    }

    private fun initNavDrawer() {
        val toggle = ActionBarDrawerToggle(
                this, drawer_layout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    private fun initRecyclerView() {
        Thread {
            val cities = AppDatabase.getInstance(this@MainActivity).citiesListDao().findAllCities()

            //add the items loaded from database
            citiesListAdapter = CitiesListAdapter(this@MainActivity, cities)
            runOnUiThread {
                recyclerCities.adapter = citiesListAdapter
            }
        }.start()
    }

    private fun showAddCityDialog() {
        AddCityDialog().show(supportFragmentManager, "TAG_CREATE")
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_add_city -> {
                //go to add city dialog
                AddCityDialog().show(supportFragmentManager, "TAG_CREATE")
            }
            R.id.nav_about -> {
                //display information about application creator
                Toast.makeText(this@MainActivity, getString(R.string.about_message), Toast.LENGTH_LONG).show()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun cityAdded(city: City) {
        Thread {
            val id = AppDatabase.getInstance(this).citiesListDao().insertCity(city)
            //update the item's id based on the generated id value so that we can access this item later
            city.cityId = id

            runOnUiThread {
                //add the item object to the recycler view
                citiesListAdapter.addCity(city)
            }
        }.start()
    }

    override fun cityUpdated(city: City) {
        val dbThread = Thread {
            //update in the database
            AppDatabase.getInstance(this@MainActivity).citiesListDao().updateCity(city)

            //update in the recycler view
            runOnUiThread { citiesListAdapter.updateCity(city, editIndex) }
        }
        dbThread.start()
    }
}
