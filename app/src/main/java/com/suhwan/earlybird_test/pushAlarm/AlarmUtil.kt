package com.suhwan.earlybird_test.pushAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object AlarmUtil {
     fun scheduleDailyAlarm(context: Context, Hour:Int, Minute:Int, Pa: String, vibration:Boolean){
        var hour = Hour
        val minute = Minute
        val pa = Pa
        if(pa =="PM" && hour != 12) hour += 12
        if(pa =="AM" && hour == 12) hour = 0

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, AlarmReceiver::class.java)
         intent.putExtra("hour", Hour)
         intent.putExtra("minute", Minute)
         intent.putExtra("pa", Pa)
         intent.putExtra("vibration", vibration)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            1001,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        // 알람 설정
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)

            // 이미 지난 시간이면 내일로 설정
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent //시간이 되었을 때 이게 실행되는거임
            )
        } else {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}