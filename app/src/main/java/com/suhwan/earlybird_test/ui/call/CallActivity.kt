package com.suhwan.earlybird_test.ui.call

import android.app.KeyguardManager
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowManager
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityCallBinding
import com.suhwan.earlybird_test.ui.timer.TimerActivity

class CallActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCallBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1){
            setShowWhenLocked(true)
            setTurnScreenOn(true)
            val keyguardManager = getSystemService(KeyguardManager::class.java)
            keyguardManager.requestDismissKeyguard(this, null)
        }
        else{
            window.addFlags(
                WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD or
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
            )
        }

        binding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            binding.button.visibility = View.INVISIBLE
            binding.ment.visibility = View.VISIBLE
            Handler(Looper.getMainLooper()).postDelayed({
                binding.title.visibility = View.INVISIBLE
                binding.ment.visibility = View.INVISIBLE
                binding.imageView4.visibility = View.INVISIBLE
                binding.count.visibility = View.VISIBLE
            }, 5000)
            var countDown = 5;
            for(i in 5 downTo 1){
                binding.count.text = countDown.toString()
                Handler(Looper.getMainLooper()).postDelayed({
                    countDown -= 1;
                }, 1000)
            }
            val intent = Intent(this, TimerActivity::class.java)
            intent.putExtra("simulateClick", true)
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}