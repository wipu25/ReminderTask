package com.example.remindertask.presentation.screens.addRemainderMap

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
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

class AddReminderMapsFragment : Fragment() {
    private lateinit var binding: FragmentAddReminderMapsBinding
    private lateinit var viewModel: AddRemainderMapViewModel
    private lateinit var locationManager: LocationManager
    private val locationListener = LocationListener {
        val current = LatLng(it.latitude, it.longitude)
        viewModel.setLocation(current)
        viewModel.setGPSLocation(false)
    }
    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.setGoogleMap(googleMap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        locationManager =
            requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        super.onCreate(savedInstanceState)
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
        val mapFragment =
            childFragmentManager.findFragmentById(binding.map.id) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        viewModel =
            ViewModelProvider(requireActivity())[AddRemainderMapViewModel(requireActivity().application)::class.java]
        getCurrentLocation()
        shouldDisplayMap()
        viewModel.apply {
            isGPSLocation.observe(viewLifecycleOwner) {
                if (!it) {
                    locationManager.removeUpdates(locationListener)
                }
            }
            selectedLocation.observe(viewLifecycleOwner) {
                if (it != null) {
                    binding.searchField.setText(it.address ?: "")
                }
            }
        }
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
            if (!granted) {
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

    private fun getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                0L,
                1f,
                locationListener
            )
        }
    }
}