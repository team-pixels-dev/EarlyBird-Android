package com.suhwan.earlybird_test.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ActivityMainBinding
import com.suhwan.earlybird_test.db.ClientManager
import com.suhwan.earlybird_test.pushAlarm.AlarmUtil
import com.suhwan.earlybird_test.ui.add.AddAlarmActivity
import com.suhwan.earlybird_test.ui.nps.NpsCommentDialogFragment
import com.suhwan.earlybird_test.ui.nps.NpsScoreDialogFragment
import com.suhwan.earlybird_test.ui.reservation.ReservationActivity
import com.suhwan.earlybird_test.ui.timer.TimerActivity

class MainActivity : AppCompatActivity() {
    companion object {
        var handler: Handler?= null
    }
    private lateinit var binding: ActivityMainBinding
    val btn_listener = View.OnClickListener { view ->
        val intent = when(view.id){
            binding.button1.id -> Intent(this, TimerActivity::class.java)
            binding.button2.id -> Intent(this, ReservationActivity::class.java)
            else -> null
        }
        intent?.let { startActivity(intent) }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        checkPermission()

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.button1.setOnClickListener(btn_listener)
        binding.button2.setOnClickListener(btn_listener)

        npsFragmentShow()

        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
    }

    private fun npsFragmentShow(){
        //TimerService와 통신을 할 때 activity가 꺼져있기 때문에 handler로 통신을 못함 -> 그래서 sharedPreferences를 사용해서 통신?을 했는데 나중에 수정하고 싶음
        val timerFinished = ClientManager.getTimerFinish(this)
        if(timerFinished == 1){
            ClientManager.setTimerFinish(this)
            val nowDate = ClientManager.setAndGetVisitDays(this)
            Log.d("nps-event","$nowDate 일 번째 접속했습니다.")
            if(nowDate==1 || nowDate==3 || nowDate ==7){
                val npsDialog = NpsScoreDialogFragment()
                npsDialog.show(supportFragmentManager,"NpsScoreDialogFragment")
            }
        }
        //NpsScoreDialogFragment와 handler를 통해서 통신했다.
        handler = Handler(Looper.getMainLooper()) { msg ->
            when (msg.what) {
                1 -> {
                    val npsDialog = NpsCommentDialogFragment()
                    npsDialog.show(supportFragmentManager,"NpsScoreDialogFragment")
                }
            }
            true // 메시지가 정상적으로 처리됨을 나타냄
        }
    }
    //다른 앱위에 그릴 수 있는 권한을 받아온다. -> 여기서는 예약시간에 전화 화면을 띄우기 위해서 사용한다.
    fun checkPermission() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(!Settings.canDrawOverlays(this)) {
                val uri = Uri.fromParts("package", packageName, null)
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, uri)
                startActivityForResult(intent, 0)
            } else {
//                val intent = Intent(applicationContext, LockScreenService::class.java)
//                startForegroundService(intent)
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0) {
            if(!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "권한을 허용해주세요", Toast.LENGTH_LONG).show()
                checkPermission()
            } else {
//                val intent = Intent(applicationContext, LockScreenService::class.java)
//                startForegroundService(intent)
            }
        }
    }
}