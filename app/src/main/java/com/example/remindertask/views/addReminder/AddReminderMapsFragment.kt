package com.example.remindertask.views.addReminder

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.remindertask.databinding.FragmentAddReminderMapsBinding
import com.example.remindertask.viewmodel.MapSearchViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddReminderMapsFragment : Fragment() {

    private lateinit var binding: FragmentAddReminderMapsBinding
    private lateinit var viewModel: MapSearchViewModel

    private val callback = OnMapReadyCallback { googleMap ->
        getCurrentLocation(googleMap)
        viewModel.selectedLocation.observe(viewLifecycleOwner) {
            if (it != null) {
                binding.searchField.setText(it.address ?: "")
                setLocation(googleMap, it.latLng!!)
            }
        }
        googleMap.setOnMapClickListener {
            viewModel.setSelectedLocation(it)
            googleMap.apply {
                clear()
                addMarker(MarkerOptions().position(it))
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddReminderMapsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel =
            ViewModelProvider(requireActivity())[MapSearchViewModel(requireActivity().application)::class.java]
        shouldDisplayMap()
        val mapFragment =
            childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        initSearchField()
        binding.saveLocation.setOnClickListener {
            this.findNavController().navigate(
                AddReminderMapsFragmentDirections.actionAddReminderMapToAddReminder(viewModel.selectedLocation.value!!)
            )
        }
    }

    private fun initSearchField() {
        val field = binding.searchField
        field.showSoftInputOnFocus = false
        field.inputType = InputType.TYPE_NULL
        field.isFocusable = false
        field.setOnClickListener {
            this.findNavController().navigate(AddReminderMapsFragmentDirections.addMapToMapSearch())
        }
    }

    private fun shouldDisplayMap() {
        val requestLocation = registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) {
                binding.noPermissionText.visibility = View.GONE
                binding.noPermissionBtn.visibility = View.GONE
                binding.map.visibility = View.VISIBLE
            } else {
                binding.noPermissionText.visibility = View.VISIBLE
                binding.noPermissionBtn.visibility = View.VISIBLE
                binding.map.visibility = View.GONE
            }
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        binding.noPermissionBtn.setOnClickListener {
            requestLocation.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun getCurrentLocation(googleMap: GoogleMap) {
        val locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationListener = LocationListener {
            val current = LatLng(it.latitude, it.longitude)
            setLocation(googleMap, current)
            viewModel.setSelectedLocation(current)
        }
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                1000L,
                1f,
                locationListener
            )
        }
    }

    private fun setLocation(googleMap: GoogleMap, latLng: LatLng) {
        googleMap.apply {
            addMarker(
                MarkerOptions().position(latLng).title("Marker in your current location")
            )
            moveCamera(CameraUpdateFactory.newLatLng(latLng))
            animateCamera(CameraUpdateFactory.zoomTo(11.0f))
        }
    }
}