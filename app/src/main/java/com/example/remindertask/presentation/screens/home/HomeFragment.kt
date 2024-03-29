package com.example.remindertask.presentation.screens.home

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.remindertask.data.models.data.ReminderForm
import com.example.remindertask.databinding.FragmentMainBinding
import com.example.remindertask.databinding.ReminderDialogBinding
import com.example.remindertask.models.HorizontalModel
import com.example.remindertask.models.VerticalItem
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentMainBinding
    private val homeViewModel: HomeViewModel by viewModels { HomeViewModel.Factory }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel.getAllReminder()


        binding = FragmentMainBinding.inflate(inflater, container, false)
        val recyclerAdapter = HomeRecyclerAdapter(::displayDialog, homeViewModel::removeItem)
//        recyclerAdapter.recyclerList = listOf(HorizontalModel(listOf<String>("a", "b", "c")))

        recyclerAdapter.notifyItemInserted(0)

        homeViewModel.allReminder.observe(viewLifecycleOwner) { it ->
            recyclerAdapter.recyclerList = it.map { e -> VerticalItem(e) }.toList()
            if (homeViewModel.removeIndex != null) {
                recyclerAdapter.notifyItemRemoved(homeViewModel.removeIndex!!)
                Toast.makeText(this.context, "Successfully remove reminder", Toast.LENGTH_SHORT)
                    .show()
            } else {
                recyclerAdapter.notifyItemRangeInserted(recyclerAdapter.recyclerList.size, it.size)
            }
            binding.loadingReminder.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
        }

        binding.recyclerView.adapter = recyclerAdapter
        binding.addReminder.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.mainToAddReminder())
        }
        return binding.root
    }

    private fun displayDialog(reminderForm: ReminderForm) {
        val bind: ReminderDialogBinding = ReminderDialogBinding.inflate(layoutInflater)
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(bind.root)

        bind.titleDialog.text = reminderForm.title
        bind.description.text = reminderForm.description

        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        bind.startDate.text = reminderForm.startDate?.format(dateTimeFormatter)
        bind.endDate.text = reminderForm.endDate?.format(dateTimeFormatter)

        if (reminderForm.alertTime != null) {
            bind.alert.text =
                "Alert Time: ${reminderForm.alertTime!!.hour} : ${reminderForm.alertTime!!.minute}"
            bind.alert.visibility = View.VISIBLE
        }
        dialog.show()

        if (reminderForm.location?.latLng != null) {
            bind.locationAddr.visibility = View.VISIBLE
            bind.map.visibility = View.VISIBLE
            bind.locationAddr.text = reminderForm.location?.address
            val mapFragment =
                childFragmentManager.findFragmentById(bind.map.id) as SupportMapFragment?
            mapFragment?.onCreate(dialog.onSaveInstanceState())
            mapFragment?.onResume()
            mapFragment?.getMapAsync { googleMap ->
                val latLng = reminderForm.location!!.latLng
                if (latLng != null) {
                    googleMap.apply {
                        addMarker(
                            MarkerOptions().position(latLng)
                                .title("Marker in your current location")
                        )
                        moveCamera(CameraUpdateFactory.newLatLng(latLng))
                        animateCamera(CameraUpdateFactory.zoomTo(12.0f))
                    }
                }
            }
        }
    }
}