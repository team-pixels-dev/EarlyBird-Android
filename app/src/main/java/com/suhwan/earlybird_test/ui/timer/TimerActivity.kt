package com.suhwan.earlybird_test.ui.timer

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityTimerBinding
import com.suhwan.earlybird_test.ui.main.MainActivity

class TimerActivity : AppCompatActivity(), TimerListener {
    private lateinit var binding: ActivityTimerBinding
    private var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTimerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        TwoMinutesTimer.listener = this

        binding.btnStart.setOnClickListener {
            if(isRunning == false){
                if(binding.btnStart.text == getString(R.string.timer_btn_end)){
                    intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    isRunning = true
                    startLockTask()
                    FullScreenMode()
                    TwoMinutesTimer.start()
                    runOnUiThread {
                        binding.btnStart.visibility = View.GONE
                        binding.tvMinute.setTextColor(getColorStateList(R.color.timer_start_text))
                        binding.tvSecond.setTextColor(getColorStateList(R.color.timer_start_text))
                        binding.tvMillisecond.setTextColor(getColorStateList(R.color.timer_start_text))
                    }
                }
            }
        }
    }

    override fun onTick(millisUntilFinished: Long) {
        val minutes = (millisUntilFinished / 1000) / 60 // 분
        val seconds = (millisUntilFinished / 1000) % 60 // 초
        val milliseconds = (millisUntilFinished % 1000) / 10 // 밀리초 (두 자리)

        runOnUiThread{
            binding.tvMinute.text = if (minutes<10) "0${minutes}" else "$minutes"
            binding.tvSecond.text = if (seconds<10) ":0${seconds}" else ":$seconds"
            binding.tvMillisecond.text = if (milliseconds<10) ":0${milliseconds}" else ":$milliseconds"
        }
    }

    override fun onFinish() {
        isRunning = false
        stopLockTask()
        ShowNavBarOnly()
        runOnUiThread{
            binding.tvMinute.text = "02"
            binding.tvSecond.text = ":00"
            binding.tvMillisecond.text = ":00"
            binding.btnStart.visibility = View.VISIBLE
            binding.tvMinute.setTextColor(getColorStateList(R.color.black))
            binding.tvSecond.setTextColor(getColorStateList(R.color.black))
            binding.tvMillisecond.setTextColor(getColorStateList(R.color.black))
            binding.tvMinute.visibility = View.INVISIBLE
            binding.tvSecond.visibility = View.INVISIBLE
            binding.tvMillisecond.visibility = View.INVISIBLE
            binding.finishGood.visibility = View.VISIBLE
            binding.btnStart.text = getString(R.string.timer_btn_end)
            binding.imageView.setImageResource(R.drawable.icon_character_2)
        }
    }

    override fun onBackPressed() {
        if(isRunning == false){
            super.onBackPressed()
        }
    }

    private fun FullScreenMode(){
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_DEFAULT
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { view, windowInsets ->
            if (windowInsets.isVisible(WindowInsetsCompat.Type.navigationBars())
                || windowInsets.isVisible(WindowInsetsCompat.Type.statusBars())) {
                windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
            }
            ViewCompat.onApplyWindowInsets(view, windowInsets)
        }
    }
    private fun ShowNavBarOnly() {
        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())
    }
}