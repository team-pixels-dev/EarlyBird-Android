package com.suhwan.earlybird_test.pushAlarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
//예약된 시간이 됐을 때 자동 호출되는 함수
class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val helper = context?.let { NotificationHelper(it) }
        helper?.deliverNotification()
    }
}