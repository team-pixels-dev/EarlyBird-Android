package com.suhwan.earlybird_test.pushAlarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.suhwan.earlybird_test.R

class NotificationHelper(private val context: Context) {
    private var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel() //클래스가 만들어질 때 알림 채널을 등록함
    }

    private fun createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        }
        val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
        notificationChannel.enableVibration(true)
        notificationChannel.description = "description"

        notificationManager.createNotificationChannel(notificationChannel)
    }
    fun deliverNotification() {

        val intent = Intent(context, TestActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID, //request Code
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.icon_timer_character_3)
            .setContentTitle("얼리버드")
            .setContentText("시작할 시간입니다")
            .setContentIntent(pendingIntent) //push 알림을 클릭했을 때 실행되는 activity
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(NOTIFICATION_ID, builder.build())
    }

    companion object {
        const val CHANNEL_ID = "default_channel"
        const val CHANNEL_NAME = "고정 알림"
        const val NOTIFICATION_ID = 0
    }
}