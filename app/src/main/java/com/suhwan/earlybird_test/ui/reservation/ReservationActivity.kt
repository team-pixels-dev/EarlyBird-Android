package com.suhwan.earlybird_test.ui.reservation

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityReservationBinding
import com.suhwan.earlybird_test.db.alarm.Alarm
import com.suhwan.earlybird_test.db.alarm.AlarmDao
import com.suhwan.earlybird_test.db.alarm.AlarmDatabase
import com.suhwan.earlybird_test.pushAlarm.AlarmReceiver
import com.suhwan.earlybird_test.pushAlarm.AlarmType
import com.suhwan.earlybird_test.pushAlarm.AlarmUtil
import com.suhwan.earlybird_test.ui.add.AddAlarmActivity
import com.suhwan.earlybird_test.ui.main.MainActivity
import java.util.Calendar

class ReservationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityReservationBinding
    lateinit var db: AlarmDatabase
    private var Hour : String = "00"
    private var Minute : String = "00"
    private var Pa : String = "AM"

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityReservationBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        AlarmUtil.scheduleDailyAlarm(this, AlarmType.MORNING)
        AlarmUtil.scheduleDailyAlarm(this, AlarmType.NIGHT)

        val HourData = mutableListOf<String>("")
        val MinuteData = mutableListOf<String>("")
        val PaData = mutableListOf<String>("","AM","PM","")
        for(i in 0..12){
            if (i<10) HourData.add("0$i") else HourData.add("$i")
        }
        for(i in 0..59){
            if (i<10) MinuteData.add("0$i") else MinuteData.add("$i")
        }
        HourData.add("")
        MinuteData.add("")

        binding.wheelPickerHour.layoutManager = LinearLayoutManager(this)
        binding.wheelPickerHour.adapter = WheelPickerAdapter(HourData)

        binding.wheelPickerMinute.layoutManager = LinearLayoutManager(this)
        binding.wheelPickerMinute.adapter = WheelPickerAdapter(MinuteData)

        binding.wheelPickerPa.layoutManager = LinearLayoutManager(this)
        binding.wheelPickerPa.adapter = WheelPickerAdapter(PaData)

        val snapHelperHour = LinearSnapHelper()
        val snapHelperMinute = LinearSnapHelper()
        val snapHelperPa = LinearSnapHelper()

        snapHelperHour.attachToRecyclerView(binding.wheelPickerHour)
        snapHelperMinute.attachToRecyclerView(binding.wheelPickerMinute)
        snapHelperPa.attachToRecyclerView(binding.wheelPickerPa)

        binding.wheelPickerHour.addOnScrollListener(getCenterItem(snapHelperHour,
            binding.wheelPickerHour.layoutManager as LinearLayoutManager,
            binding.wheelPickerHour.adapter as WheelPickerAdapter,
            1
        ))
        binding.wheelPickerMinute.addOnScrollListener(getCenterItem(snapHelperMinute,
            binding.wheelPickerMinute.layoutManager as LinearLayoutManager,
            binding.wheelPickerMinute.adapter as WheelPickerAdapter,
            2
        ))
        binding.wheelPickerPa.addOnScrollListener(getCenterItem(snapHelperPa,
            binding.wheelPickerPa.layoutManager as LinearLayoutManager,
            binding.wheelPickerPa.adapter as WheelPickerAdapter,
            3
        ))

        binding.btnFinish.setOnClickListener {
            val vibration = binding.switchVibration.isChecked

            AlarmUtil.scheduleDailyAlarm(
                this,
                AlarmType.USER,
                customHour = Hour.toInt(),
                customMinute = Minute.toInt(),
                customPa = Pa,
                customVibration = vibration
            )

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnBack.setOnClickListener{
            finish()
        }

        binding.switchVibration.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked){
                binding.switchVibration.trackTintList = ContextCompat.getColorStateList(this,R.color.sub_background)
            }
            else{
                binding.switchVibration.trackTintList = ContextCompat.getColorStateList(this,R.color.gray)
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun getCenterItem(snapHelper : LinearSnapHelper, layoutManager: LinearLayoutManager, adapter: WheelPickerAdapter, select: Int) : RecyclerView.OnScrollListener {
        return object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    val centerView = snapHelper.findSnapView(layoutManager)
                    centerView?.let {
                        val centerPosition = layoutManager.getPosition(it)
                        val item = adapter.getItem(centerPosition)
                        adapter.setSelectedPosition(centerPosition)
                        when(select){
                            1 -> Hour = item
                            2 -> Minute = item
                            3 -> Pa = item
                        }
                    }
                }
            }
        }
    }
}