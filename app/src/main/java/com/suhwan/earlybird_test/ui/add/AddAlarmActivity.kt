package com.suhwan.earlybird_test.ui.add

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityAddAlarmBinding
import com.suhwan.earlybird_test.db.Alarm
import com.suhwan.earlybird_test.db.AlarmDao
import com.suhwan.earlybird_test.db.AlarmDatabase
import com.suhwan.earlybird_test.ui.reservation.ReservationActivity

class AddAlarmActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddAlarmBinding
    private lateinit var db: AlarmDatabase
    private lateinit var alarmDao: AlarmDao
    private lateinit var alarmList: ArrayList<Alarm>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityAddAlarmBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAddSchedule.setOnClickListener {
            intent = Intent(this, ReservationActivity::class.java)
            startActivity(intent)
        }

        db = AlarmDatabase.getDatabase(this)
        alarmDao = db.getAlarmDao()

        getAllAlarmList()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun getAllAlarmList(){
        Thread{
            alarmList = ArrayList(alarmDao.getAllAlarm())
            if (alarmList.isEmpty())
                binding.zeroList.visibility = View.VISIBLE
            else{
                binding.zeroList.visibility = View.INVISIBLE
                setRecycleView()
            }
        }.start()
    }
    private fun setRecycleView(){
        runOnUiThread {
            binding.recycleSchedule.adapter = CalendarAdapter(alarmList)
            binding.recycleSchedule.layoutManager = LinearLayoutManager(this)
            binding.recycleSchedule.addItemDecoration(CalendarDecoration(this))
        }
    }
}