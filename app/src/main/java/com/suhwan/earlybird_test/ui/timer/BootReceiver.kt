package com.suhwan.earlybird_test.ui.timer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val launchIntent = Intent(context, TimerActivity::class.java)
            launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context?.startActivity(launchIntent)
        }
    }
}