package com.example.remindertask.presentation.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.remindertask.databinding.HorizontalItemBinding

class HomeHorizontalAdapter(private val list: List<String>) :
    RecyclerView.Adapter<HomeHorizontalAdapter.ViewHolder>() {
    class ViewHolder(private val item: HorizontalItemBinding) : RecyclerView.ViewHolder(item.root) {
        fun bind(text: String) {
            item.textHead.text = text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val item = HorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(item)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }
}