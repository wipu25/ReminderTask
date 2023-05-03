package com.example.remindertask.models.data

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.parcelize.Parcelize

@Parcelize
data class SelectedLocation(
    val latLng: LatLng?,
    val address: String?
) : Parcelable