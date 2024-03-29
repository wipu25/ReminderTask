package com.example.remindertask.presentation.screens.addRemainderMap.mapSearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remindertask.databinding.MapSearchItemBinding
import com.google.android.libraries.places.api.model.AutocompletePrediction

class MapSearchRecyclerAdapter(private val onClickListener: (AutocompletePrediction) -> Unit) :
    RecyclerView.Adapter<MapSearchRecyclerAdapter.MapSearchItem>() {

    private var resultList: List<AutocompletePrediction> = listOf()

    class MapSearchItem(private val item: MapSearchItemBinding, onClickListener: (Int) -> Unit) :
        RecyclerView.ViewHolder(item.root) {
        init {
            item.root.setOnClickListener {
                onClickListener(absoluteAdapterPosition)
            }
        }

        fun bind(head: String) {
            item.locationName.text = head
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapSearchItem {
        val itemBinding =
            MapSearchItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MapSearchItem(itemBinding) {
            onClickListener(resultList[it])
        }
    }

    override fun onBindViewHolder(holder: MapSearchItem, position: Int) {
        holder.bind(resultList[position].getFullText(null).toString())
    }

    override fun getItemCount(): Int {
        return resultList.size
    }

    fun updateList(resultList: List<AutocompletePrediction>) {
        this.resultList = resultList
        notifyItemRangeInserted(0, resultList.size)
    }
}