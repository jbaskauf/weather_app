package edu.carleton.baskaufj.weatherapp.data

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo
import java.io.Serializable

@Entity(tableName = "city")
data class City(
        @PrimaryKey(autoGenerate = true) var cityId: Long?,
        @ColumnInfo(name = "name") var name: String,
        @ColumnInfo(name = "currenttemp") var currentTemp: String,
        @ColumnInfo(name = "weathericon") var weatherIcon: String
) : Serializable