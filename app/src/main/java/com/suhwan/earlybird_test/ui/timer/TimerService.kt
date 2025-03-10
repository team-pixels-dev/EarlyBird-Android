package com.suhwan.earlybird_test.ui.timer

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Binder
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageButton
import androidx.core.app.NotificationCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.databinding.ViewInTimerServiceBinding
import com.suhwan.earlybird_test.ui.main.MainActivity

class TimerService : Service(), TimerListener{
    private var binding: ViewInTimerServiceBinding? = null
    private var wm: WindowManager? = null
    private var overlayView: View? = null
    private val handler = Handler(Looper.getMainLooper())

    private val SERVICE_ID = 1
    private val channelId = "TIMER_CHANNEL"

    override fun onBind(intent: Intent?): IBinder?{
      return null

    }
    override fun onCreate() {
        super.onCreate()
        TwoMinutesTimer.listener = this
        TwoMinutesTimer.start()
        showOverlay()
    }

    private fun showOverlay() {
        wm = getSystemService(WINDOW_SERVICE) as WindowManager
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        binding = ViewInTimerServiceBinding.inflate(inflater)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY, // 다른 앱 위에 표시
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.CENTER

        wm!!.addView(binding!!.root, params)

        binding?.btnEnd?.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            stopSelf()
        }
    }
    private fun removeOverlay() {
        if (wm != null && binding != null) {
            wm!!.removeView(binding!!.root)
            binding = null
            wm = null
        }
    }

    @SuppressLint("DefaultLocale")
    override fun onTick(millisUntilFinished: Long) {
        Log.d("Timer","time")
        val minutes = (millisUntilFinished / 1000) / 60 // 분
        val seconds = (millisUntilFinished / 1000) % 60 // 초
        val milliseconds = (millisUntilFinished % 1000) / 10 // 밀리초 (두 자리)
        handler.post{
            binding?.tvMinute?.text = String.format("%02d", minutes)
            binding?.tvSecond?.text = String.format(":%02d", seconds)
            binding?.tvMillisecond?.text = String.format(".%02d", milliseconds)
        }

    }
    override fun onFinish() {
        timerFinishUi()
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "타이머 채널",
                NotificationManager.IMPORTANCE_LOW //?
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        removeOverlay() // 서비스가 종료될 때 오버레이 제거
        handler.removeCallbacksAndMessages(null) // 핸들러 제거
        TwoMinutesTimer.listener = null
    }

    private fun timerFinishUi(){
        handler.post {
            binding?.tvMinute?.visibility = View.INVISIBLE
            binding?.tvSecond?.visibility = View.INVISIBLE
            binding?.tvMillisecond?.visibility = View.INVISIBLE
            binding?.finishGood?.visibility = View.VISIBLE
            binding?.btnEnd?.visibility = View.VISIBLE
            binding?.imageView?.setImageResource(R.drawable.icon_character_2)
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////
    private fun createNotification(content: String): Notification {
        val intent = Intent(this, TimerActivity::class.java) //이게 notification 눌렀을 때 이동할 곳
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("2분 타이머")
            .setContentText(content)
            .setSmallIcon(R.drawable.icon_character_1)
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }
    private fun updateNotification(content: String) {
        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.notify(SERVICE_ID, createNotification(content))
    }
}