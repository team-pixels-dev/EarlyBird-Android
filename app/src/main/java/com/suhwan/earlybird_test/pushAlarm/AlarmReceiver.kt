package com.suhwan.earlybird_test.pushAlarm

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.PowerManager
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat.getSystemService
import com.suhwan.earlybird_test.ui.call.CallActivity

//예약된 시간이 됐을 때 자동 호출되는 함수
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val hour : Int = intent.getIntExtra("hour", 9)
        val minute : Int = intent.getIntExtra("minute", 0)
        val pa : String  = intent.getStringExtra("pa") ?: "AM"
        val vibration : Boolean = intent.getBooleanExtra("vibration", false)
        val requestCode : Int = intent.getIntExtra("requestCode", -1)

        val helper = context.let { NotificationHelper(it) }
        if(requestCode == AlarmType.USER_REQUEST_CODE){
            helper.deliverCustomNotification()

            val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val wakeLock = powerManager.newWakeLock(
                        PowerManager.SCREEN_BRIGHT_WAKE_LOCK or
                                PowerManager.ACQUIRE_CAUSES_WAKEUP,
                "EarlyBird::TimerWakeLock"
            )
            val callIntent = Intent(context, CallActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TOP or
                        Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            context.startActivity(callIntent)
            if (context is Activity) {
                context.window.addFlags(
                    WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON or
                            WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED or
                            WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                )
            }
        }
        else{
            helper.deliverDefaultNotification()
        }

        val alarmType = AlarmType.fromRequestCode(requestCode)
        if (alarmType != null) {
            AlarmUtil.scheduleDailyAlarm(context, alarmType, hour, minute, pa, vibration)
        }
    }
}