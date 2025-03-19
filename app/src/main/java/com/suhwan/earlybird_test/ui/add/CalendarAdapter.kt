package com.suhwan.earlybird_test.ui.add

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.suhwan.earlybird_test.databinding.ItemCalendarBinding
import com.suhwan.earlybird_test.db.alarm.Alarm

class CalendarAdapter(private val alarmList: ArrayList<Alarm>) :
    RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    inner class CalendarViewHolder(val binding: ItemCalendarBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val binding = ItemCalendarBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CalendarViewHolder(binding)
    }

    //getItemCount()는 항목개수를 판단하려고 자동으로 호출된다.
    override fun getItemCount(): Int = alarmList.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val alarmData = alarmList[position]
        val hour : String = if(alarmData.hour<10) "0${alarmData.hour}" else  "${alarmData.hour}"
        val minute : String = if(alarmData.minute<10) "0${alarmData.minute}" else  "${alarmData.minute}"
        with(holder.binding){
            content.text = alarmData.todo
            time.text = "$hour:$minute"
            pa.text = alarmData.pa
        }
    }
}