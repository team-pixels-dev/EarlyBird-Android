package com.suhwan.earlybird_test.pushAlarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object AlarmUtil {
     fun scheduleDailyAlarm(
         context: Context,
         alarmType: AlarmType,
         customHour: Int? = null,
         customMinute: Int? = null,
         customPa: String? = null,
         customVibration: Boolean? = null
     ) {
         //기본 알람일 때랑 사용자가 알림을 보낼 때랑 alarmType을 입력받느냐에 따라서 달라짐
         val hour = customHour ?: alarmType.defaultHour
         val minute = customMinute ?: alarmType.defaultMinute
         val pa = customPa ?: alarmType.pa
         val vibration = customVibration ?: alarmType.vibration

         var hour24 = if (pa == "PM" && hour != 12) hour + 12 else if (pa == "AM" && hour == 12) 0 else hour

         val intent = Intent(context, AlarmReceiver::class.java).apply {
             putExtra("hour", hour24)
             putExtra("minute", minute)
             putExtra("pa", pa)
             putExtra("vibration", vibration)
             putExtra("requestCode", alarmType.requestCode)
         }

         val pendingIntent = PendingIntent.getBroadcast(
             context,
             alarmType.requestCode,
             intent,
             PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
         )

         // 알람 설정
         val calendar = Calendar.getInstance().apply {
             set(Calendar.HOUR_OF_DAY, hour24)
             set(Calendar.MINUTE, minute)
             set(Calendar.SECOND, 0)
             set(Calendar.MILLISECOND, 0)

             // 이미 지난 시간이면 내일로 설정
             if (timeInMillis <= System.currentTimeMillis()) {
                 add(Calendar.DAY_OF_YEAR, 1)
             }
         }

         val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

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