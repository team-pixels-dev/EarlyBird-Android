package com.suhwan.earlybird_test.pushAlarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

//예약된 시간이 됐을 때 자동 호출되는 함수
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val hour : Int = intent.getIntExtra("hour", 9)
        val minute : Int = intent.getIntExtra("minute", 0)
        val pa : String  = intent.getStringExtra("pa") ?: "AM"
        val vibration : Boolean = intent.getBooleanExtra("vibration", false)

        Log.d("receiver", "$hour 시 $minute 분 $pa")
        val helper = context.let { NotificationHelper(it) }
        if(vibration){
            helper.deliverDefaultNotification()
        }
        else{
            helper.deliverCustomNotification()
        }
        AlarmUtil.scheduleDailyAlarm(context, hour,minute,pa, vibration)
    }
}