package com.example.remindertask.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.remindertask.R
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class MapSearchViewModel(application: Application) : AndroidViewModel(application) {
    private var _placesClient : PlacesClient
    private var _resultLiveDate: MutableLiveData<List<AutocompletePrediction>> = MutableLiveData(
        mutableListOf()
    )
    private var _selectedLocation: MutableLiveData<AutocompletePrediction> = MutableLiveData()
    val resultLiveData: LiveData<List<AutocompletePrediction>>
        get() = _resultLiveDate

    init {
        val application = getApplication<Application>()
        Places.initialize(application.applicationContext,application.resources.getString(R.string.api_key_google_map))
        _placesClient = Places.createClient(application.applicationContext)
    }

    fun searchPlaces(text: String) {
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setCountries("TH")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery(text)
                .build()
        _placesClient.findAutocompletePredictions(request)
            .addOnSuccessListener { response: FindAutocompletePredictionsResponse ->
                _resultLiveDate.value = response.autocompletePredictions
            }.addOnFailureListener { exception: Exception? ->
                if (exception is ApiException) {
                    Log.e("err", "Place not found: ${exception.statusCode}")
                }
            }
    }
}