package com.example.remindertask.presentation.screens.addRemainderMap

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.remindertask.R
import com.example.remindertask.data.models.data.SelectedLocation
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddRemainderMapViewModel(application: Application) : AndroidViewModel(application) {
    private var _placesClient: PlacesClient
    private var _resultLiveDate: MutableLiveData<List<AutocompletePrediction>> = MutableLiveData(
        mutableListOf()
    )
    private var _selectedLocation: MutableLiveData<SelectedLocation> = MutableLiveData()
    private var _isGPSLocation: MutableLiveData<Boolean> = MutableLiveData(true)
    private var _googleMap: GoogleMap? = null
    val resultLiveData: LiveData<List<AutocompletePrediction>>
        get() = _resultLiveDate
    val selectedLocation: LiveData<SelectedLocation>
        get() = _selectedLocation
    val isGPSLocation: LiveData<Boolean>
        get() = _isGPSLocation

    init {
        val applicationState = getApplication<Application>()
        Places.initialize(
            applicationState.applicationContext,
            application.resources.getString(R.string.api_key_google_map)
        )
        _placesClient = Places.createClient(application.applicationContext)
    }

    fun searchPlaces(text: String) {
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder()
                .setCountries("TH")
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

    fun setSelectedPlaces(autocompletePrediction: AutocompletePrediction) {
        val placeFields = listOf(
            Place.Field.NAME,
            Place.Field.ID,
            Place.Field.LAT_LNG,
            Place.Field.ADDRESS
        )
        viewModelScope.launch {
            val fetchPlaceRequest =
                FetchPlaceRequest.newInstance(autocompletePrediction.placeId, placeFields)
            val result = _placesClient.fetchPlace(fetchPlaceRequest).await()
            setLocation(result.place.latLng!!, result.place.address)
        }
    }

    fun setLocation(latLng: LatLng, address: String? = null) {
        _selectedLocation.value = SelectedLocation(latLng, address ?: "My Pin Location")
        _googleMap?.apply {
            clear()
            addMarker(
                MarkerOptions().position(latLng).title("Marker in your current location")
            )
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.zoomTo(11.0f))
            setOnMapClickListener {
                setLocation(it)
            }
        }
    }

    fun setGPSLocation(boolean: Boolean) {
        _isGPSLocation.value = boolean
    }

    fun setGoogleMap(googleMap: GoogleMap) {
        _googleMap = googleMap
        googleMap.apply {
            setOnMapClickListener {
                setLocation(it)
            }
        }
    }
}