package com.example.remindertask.views.addRemainder

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.remindertask.databinding.FragmentAddRemainderMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddRemainderMapsFragment : Fragment() {

    private lateinit var binding: FragmentAddRemainderMapsBinding

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-34.0, 151.0)
        googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRemainderMapsBinding.inflate(layoutInflater)
        shouldDisplayMap()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment =
            childFragmentManager.findFragmentById(binding.root.id) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun shouldDisplayMap() {
        val requestLocation = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) {
                binding.noPermissionText.visibility = View.GONE
                binding.noPermissionBtn.visibility = View.GONE
                binding.addReminderMap.visibility = View.VISIBLE
            } else {
                binding.noPermissionText.visibility = View.VISIBLE
                binding.noPermissionBtn.visibility = View.VISIBLE
                binding.addReminderMap.visibility = View.GONE
            }
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

        binding.noPermissionBtn.setOnClickListener {
            requestLocation.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
}