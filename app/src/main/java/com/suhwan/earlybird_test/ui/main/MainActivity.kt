package com.suhwan.earlybird_test.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityMainBinding
import com.suhwan.earlybird_test.ui.add.AddAlarmActivity
import com.suhwan.earlybird_test.ui.reservation.ReservationActivity
import com.suhwan.earlybird_test.ui.timer.TimerActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val btn_listener = View.OnClickListener { view ->
        val intent = when(view.id){
            binding.button1.id -> Intent(this, TimerActivity::class.java)
            binding.button2.id -> Intent(this, AddAlarmActivity::class.java)
//            binding.button2.id -> Intent(this, ReservationActivity::class.java)
            else -> null
        }
        intent?.let { startActivity(intent) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        binding.button1.setOnClickListener(btn_listener)
        binding.button2.setOnClickListener(btn_listener)

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}