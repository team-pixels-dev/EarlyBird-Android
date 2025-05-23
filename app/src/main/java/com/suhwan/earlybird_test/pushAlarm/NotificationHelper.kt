package com.suhwan.earlybird_test.pushAlarm

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.suhwan.earlybird_test.R
import com.suhwan.earlybird_test.ui.main.MainActivity
import com.suhwan.earlybird_test.ui.timer.TimerActivity
import java.util.Calendar

class NotificationHelper(private val context: Context) {
    private var notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createNotificationChannel() //클래스가 만들어질 때 알림 채널을 등록함
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 기본 알림 채널
            val defaultChannel = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                CHANNEL_NAME_DEFAULT,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "정기 알림"
                enableVibration(true)
            }

            // 사용자 지정 알림 채널
            val customChannel = NotificationChannel(
                CHANNEL_ID_CUSTOM,
                CHANNEL_NAME_CUSTOM,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "사용자가 알림"
                enableVibration(false)
            }

            notificationManager.createNotificationChannel(defaultChannel)
            notificationManager.createNotificationChannel(customChannel)
        }
    }
    fun deliverDefaultNotification() {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        sendNotification(
            title = "얼리버드 알림",
            text = "지금은 정기 알림 시간입니다.",
            channelId = CHANNEL_ID_DEFAULT,
            notificationId = NOTIFICATION_ID_DEFAULT,
            intent
        )
    }

    fun deliverCustomNotification() {
        val intent = Intent(context, TimerActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        sendNotification(
            title = "사용자 알림",
            text = "사용자 설정 시간 알림입니다.",
            channelId = CHANNEL_ID_CUSTOM,
            notificationId = NOTIFICATION_ID_CUSTOM,
            intent
        )
    }
    private fun sendNotification(title: String, text: String, channelId: String, notificationId: Int, intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.icon_timer_character_3)
            .setContentTitle(title)
            .setContentText(text)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setDefaults(NotificationCompat.DEFAULT_ALL)

        notificationManager.notify(notificationId, builder.build())
    }
    companion object {
        const val CHANNEL_ID_DEFAULT = "default_channel"
        const val CHANNEL_NAME_DEFAULT = "고정 알림"

        const val CHANNEL_ID_CUSTOM = "custom_channel" //진동이 없는 채널
        const val CHANNEL_NAME_CUSTOM = "사용자 지정 알림"

        const val NOTIFICATION_ID_DEFAULT = 100
        const val NOTIFICATION_ID_CUSTOM = 200
    }
}