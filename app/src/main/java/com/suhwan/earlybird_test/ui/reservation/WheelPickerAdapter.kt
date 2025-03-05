package com.suhwan.earlybird_test.ui.reservation

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suhwan.earlybird_test.databinding.ItemWheelPickerBinding

class WheelPickerAdapter(private val items: List<String>) : RecyclerView.Adapter<WheelPickerAdapter.WheelPickerViewHolder>(){
    private var selectedPosition = 1

    inner class WheelPickerViewHolder(val binding:ItemWheelPickerBinding) :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WheelPickerViewHolder {
        val binding = ItemWheelPickerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WheelPickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WheelPickerViewHolder, position: Int) {
        val item = items[position]
        holder.binding.itemBox.text = item

        if(position == selectedPosition){
            holder.binding.itemBox.setTextColor(Color.parseColor("#000000"))
        } else{
            holder.binding.itemBox.setTextColor(Color.parseColor("#D3D3D3"))
        }

    }
    override fun getItemCount(): Int {
        return items.size
    }

    fun getItem(position: Int): String{
        return items[position]
    }

    fun setSelectedPosition(position: Int){
        selectedPosition = position
        notifyDataSetChanged()
    }
}