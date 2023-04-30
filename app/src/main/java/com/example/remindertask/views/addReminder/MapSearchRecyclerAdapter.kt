package com.example.remindertask.views.addReminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remindertask.databinding.MapSearchItemBinding

class MapSearchRecyclerAdapter: RecyclerView.Adapter<MapSearchRecyclerAdapter.MapSearchItem>() {

    class MapSearchItem(private val item: MapSearchItemBinding): RecyclerView.ViewHolder(item.root) {
        fun bind(head: String) {
            item.locationName.text = head
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapSearchItem {
        val itemBinding = MapSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MapSearchItem(itemBinding)
    }

    override fun onBindViewHolder(holder: MapSearchItem, position: Int) {
        holder.bind("sa")
    }

    override fun getItemCount(): Int {
       return 3
    }
}