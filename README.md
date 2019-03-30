# Weather App
This weather app is a class project from Mobile Software Development (taught by Péter Ekler at AIT Budapest). It is an Android application that allows users to view current weather conditions in major cities and add and remove cities to their weather homepage.

## Features
This application uses a RecyclerView to store the cities that the user has added to their homepage and is backed by a Room database which updates automatically when the user adds or removes cities from their list. The user can also click on a city to view detailed weather information for that city.  The app makes calls to the OpenWeatherMap API to retrieve weather information and icons.

## Repo Structure
For simplicity, only relevant files and directories (i.e. files created or modified by me, not auto-generated by Android Studio) are included in this diagram.
```
├── README.md                           : Description of this repository
│
└── WeatherApp/app                  	  : Project files
    ├── src/main                    
    │   ├── java/…/weatherapp       	  : Primary Kotlin classes for the project
    │   │   ├── adapter                 : CitiesListAdapter.kt (adapter for the RecyclerView)
    │   │   │
    │   │   ├── data                    : Kotlin classes for database elements
    │   │   │   ├── AppDatabase.kt      : Database holder
    │   │   │   ├── CitiesListDAO.kt    : DAO with database queries
    │   │   │   ├── City.kt             : City entity class
    │   │   │   └── WeatherResult.kt    : Data classes for results received from API calls
    │   │   │
    │   │   ├── network/WeatherAPI.kt   : Interface for GET queries to the API 
    │   │   │
    │   │   ├── AddCityDialog.kt        : Dialog fragment for adding cities
    │   │   ├── DetailsActivity.kt 	 : Activity for viewing weather details for a specific city
    │   │   └── MainActivity.kt 		 : Primary homepage list activity
    │   │
    │   ├── res                         : companion resource files for the project
    │   │   ├── drawable                : xml files for launcher icon
    │   │   ├── layout                  : xml layout files for activities, dialogs, and inflated layouts
    │   │   ├── menu                    : main menu xml file
    │   │   ├── mipmap-...              : launcher icon versions for different screen densities
    │   │   └── values                  : xml files for extracted strings and customized styles
    │   │
    │   └── AndroidManifest.xml         : manifest file for the application
    │
    └── build.gradle                    : gradle file for the app module
```

## Future Improvements
I attempted to add a feature where a brief summary of weather conditions in each city could be viewed from the list on the homepage, but refreshing this information every time the user went to the homepage caused too many API calls for my free key, so the current version does not include this feature, although I have left my work on the feature in comments for now. Since I was not able to fully test this feature, it has some bugs (for example, it only seems to call the API when the city is first added, rather than updating the weather info periodically), so it would need improving if I were to actually implement it.
I would also like to incorporate several TouchHelper elements. I would like to implement a swipe to delete feature, and I would like to introduce the capability to move items around on the list by dragging and dropping.

### Contributors

Jessie Baskauf

Some basic code adapted from Péter Ekler's demo projects

