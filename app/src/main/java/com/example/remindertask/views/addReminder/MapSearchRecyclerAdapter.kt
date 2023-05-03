package com.example.remindertask.views.addReminder

import android.util.Log
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remindertask.databinding.MapSearchItemBinding
import com.google.android.libraries.places.api.model.AutocompletePrediction

class MapSearchRecyclerAdapter(private val onClickListener: OnClickListener): RecyclerView.Adapter<MapSearchRecyclerAdapter.MapSearchItem>() {

    var resultList: List<AutocompletePrediction> = listOf()
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
        holder.itemView.setOnClickListener {
            onClickListener.onClick(getItemId(position))
        }
        holder.bind(resultList[position].getFullText(null).toString())
    }

    override fun getItemCount(): Int {
       return resultList.size
    }

    fun updateList(resultList: List<AutocompletePrediction>) {
        this.resultList = resultList
        notifyItemRangeInserted(0,resultList.size)
    }
}