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
        val requestCode : Int = intent.getIntExtra("requestCode", -1)

        val helper = context.let { NotificationHelper(it) }
        if(requestCode == AlarmType.USER_REQUEST_CODE){
            helper.deliverCustomNotification()
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