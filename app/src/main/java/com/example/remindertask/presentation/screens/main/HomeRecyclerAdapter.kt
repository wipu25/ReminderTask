package com.example.remindertask.presentation.screens.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.remindertask.data.models.data.ReminderForm
import com.example.remindertask.databinding.HorizontalRecyclerBinding
import com.example.remindertask.databinding.RecyclerItemBinding
import com.example.remindertask.models.HorizontalModel
import com.example.remindertask.models.RecyclerItem
import com.example.remindertask.models.Type
import com.example.remindertask.models.VerticalItem

class HomeRecyclerAdapter(
    private val onPressCallback: (reminderForm: ReminderForm) -> Unit,
    private val removeItem: (uid: Int, position: Int) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var recyclerList: List<RecyclerItem> = listOf()

    private object Const {
        const val HORIZONTAL = 0 // random unique value
        const val VERTICAL = 1
    }

    class HorizontalHolder(private val recyclerItem: HorizontalRecyclerBinding) :
        RecyclerView.ViewHolder(recyclerItem.root) {
        fun bind(list: List<String>) {
            recyclerItem.horizontaRecycler.adapter = HomeHorizontalAdapter(list)
        }
    }

    class VerticalHolder(
        private val recyclerItem: RecyclerItemBinding,
        private val onPressCallback: (reminderForm: ReminderForm) -> Unit,
        private val removeItem: (uid: Int, position: Int) -> Unit
    ) :
        RecyclerView.ViewHolder(recyclerItem.root) {
        fun bind(reminderForm: ReminderForm, position: Int) {
            recyclerItem.text.text = reminderForm.title
            recyclerItem.description.text = reminderForm.description
            recyclerItem.root.setOnClickListener {
                onPressCallback(reminderForm)
            }
            recyclerItem.bin.setOnClickListener {
                removeItem(reminderForm.uid, position)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Const.HORIZONTAL) {
            val itemBinding = HorizontalRecyclerBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            HorizontalHolder(itemBinding)
        } else {
            val itemBinding =
                RecyclerItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            VerticalHolder(itemBinding, onPressCallback, removeItem)
        }
    }

    override fun getItemCount(): Int {
        return recyclerList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams
        if (getItemViewType(position) == Const.HORIZONTAL) {
            layoutParams.isFullSpan = true
            (holder as HorizontalHolder).bind((recyclerList[position] as HorizontalModel).list)
        } else {
            (holder as VerticalHolder).bind(
                (recyclerList[position] as VerticalItem).reminderForm,
                position
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (recyclerList[position].type == Type.horizontal) Const.HORIZONTAL else Const.VERTICAL
    }
}