package com.example.remindertask.data.models.data

import android.os.Parcelable
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedLocation(
    val latLng: LatLng? = null,
    val address: String? = null
) : Parcelable

class SelectedLocationConverter {
    @TypeConverter
    fun StringToSelectedLocation(string: String?): SelectedLocation? {
        return Gson().fromJson(string, SelectedLocation::class.java)
    }

    @TypeConverter
    fun SelectedLocationToString(selectedLocation: SelectedLocation?): String? {
        return Gson().toJson(selectedLocation)
    }
}