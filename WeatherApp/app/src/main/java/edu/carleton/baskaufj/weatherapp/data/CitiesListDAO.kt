package edu.carleton.baskaufj.weatherapp.data

import android.arch.persistence.room.*

@Dao
interface CitiesListDAO {

    @Query("SELECT * FROM city")
    fun findAllCities(): List<City>

    //returns the ID of the item inserted
    @Insert
    fun insertCity(city: City) : Long

    @Delete
    fun deleteCity(city: City)

    @Update
    fun updateCity(city: City)

}