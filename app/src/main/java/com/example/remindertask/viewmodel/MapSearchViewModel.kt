package com.example.remindertask.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient

class MapSearchViewModel(placesClient: PlacesClient) : ViewModel() {
    private var _placesClient : PlacesClient
    private var _resultLiveDate: MutableLiveData<List<AutocompletePrediction>> = MutableLiveData(
        mutableListOf()
    )
    val resultLiveData: LiveData<List<AutocompletePrediction>>
        get() = _resultLiveDate
    init {
        _placesClient = placesClient
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